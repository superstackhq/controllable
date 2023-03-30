package one.superstack.controllable.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.auth.actor.Jwt;
import one.superstack.controllable.auth.actor.RequiresAuthentication;
import one.superstack.controllable.auth.actor.ThreadLocalWrapper;
import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.auth.app.RequiresAppAuthentication;
import one.superstack.controllable.exception.InvalidTokenException;
import one.superstack.controllable.exception.ServerException;
import one.superstack.controllable.service.ApiKeyService;
import one.superstack.controllable.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Jwt jwt;

    private final ApiKeyService apiKeyService;

    private final AppService appService;

    private static final String BEARER_PREFIX = "Bearer";
    private static final String API_KEY_PREFIX = "ApiKey";
    private static final String APP_KEY_PREFIX = "AppKey";

    @Autowired
    public AuthInterceptor(Jwt jwt, ApiKeyService apiKeyService, AppService appService) {
        this.jwt = jwt;
        this.apiKeyService = apiKeyService;
        this.appService = appService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Object handlerBean = ((HandlerMethod) handler).getBean();

            if (handlerBean instanceof RequiresAppAuthentication) {
                AuthorizationHeader header = extractAuthorizationData(request);
                AuthenticatedApp app = getApp(header);
                one.superstack.controllable.auth.app.ThreadLocalWrapper.setApp(app);
            } else if (handlerBean instanceof RequiresAuthentication) {
                AuthorizationHeader header = extractAuthorizationData(request);
                AuthenticatedActor actor = getActor(header);
                ThreadLocalWrapper.setActor(actor);
            }
        }

        return true;
    }

    private AuthenticatedApp getApp(AuthorizationHeader header) {
        if (!APP_KEY_PREFIX.equals(header.getType())) {
            throw new InvalidTokenException();
        }

        try {
            return appService.getByAccessKey(header.getContent());
        } catch (Throwable e) {
            throw new InvalidTokenException();
        }
    }

    private AuthenticatedActor getActor(AuthorizationHeader header) {
        AuthenticatedActor actor;

        switch (header.getType()) {
            case BEARER_PREFIX -> {
                String jwtToken = header.getContent();
                actor = jwt.getActor(jwtToken);
                if (null == actor) {
                    throw new InvalidTokenException();
                }
            }

            case API_KEY_PREFIX -> {
                String apiKey = header.getContent();
                try {
                    actor = apiKeyService.getByAccessKey(apiKey);
                } catch (Throwable e) {
                    throw new ServerException(e.getMessage());
                }
                ThreadLocalWrapper.setActor(actor);
            }

            default -> throw new InvalidTokenException();
        }

        return actor;
    }

    private AuthorizationHeader extractAuthorizationData(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new InvalidTokenException();
        }

        String[] authorizationComponents = authorizationHeader.split("\\s+");

        if (authorizationComponents.length != 2) {
            throw new InvalidTokenException();
        }

        String tokenType = authorizationComponents[0];
        if (!tokenType.equals(BEARER_PREFIX) && !tokenType.equals(API_KEY_PREFIX) && !tokenType.equals(APP_KEY_PREFIX)) {
            throw new InvalidTokenException();
        }

        return new AuthorizationHeader(tokenType, authorizationComponents[1]);
    }
}