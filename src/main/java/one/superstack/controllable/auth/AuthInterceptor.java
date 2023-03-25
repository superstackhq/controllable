package one.superstack.controllable.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.superstack.controllable.exception.InvalidTokenException;
import one.superstack.controllable.exception.ServerException;
import one.superstack.controllable.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Jwt jwt;

    private final ApiKeyService apiKeyService;

    @Autowired
    public AuthInterceptor(Jwt jwt, ApiKeyService apiKeyService) {
        this.jwt = jwt;
        this.apiKeyService = apiKeyService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Object handlerBean = ((HandlerMethod) handler).getBean();

            if (handlerBean instanceof RequiresAuthentication) {
                AuthorizationHeader header = extractAuthorizationData(request);

                AuthenticatedActor actor;

                switch (header.getType()) {
                    case "Bearer" -> {
                        String jwtToken = header.getContent();
                        actor = jwt.getActor(jwtToken);
                        if (null == actor) {
                            throw new InvalidTokenException();
                        }
                    }

                    case "ApiKey" -> {
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

                ThreadLocalWrapper.setActor(actor);
            }
        }

        return true;
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
        if (!tokenType.equals("Bearer") && !tokenType.equals("ApiKey")) {
            throw new InvalidTokenException();
        }

        return new AuthorizationHeader(tokenType, authorizationComponents[1]);
    }
}