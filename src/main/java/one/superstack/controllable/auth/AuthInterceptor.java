package one.superstack.controllable.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.superstack.controllable.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Jwt jwt;

    @Autowired
    public AuthInterceptor(Jwt jwt) {
        this.jwt = jwt;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Object handlerBean = ((HandlerMethod) handler).getBean();

            if (handlerBean instanceof RequiresUserAuthentication) {
                String jwtToken = extractToken(request);

                AuthenticatedUser user = jwt.getUser(jwtToken);

                if (null == user) {
                    throw new InvalidTokenException();
                }

                ThreadLocalWrapper.setUser(user);
            }
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new InvalidTokenException();
        }

        String[] authorizationComponents = authorizationHeader.split("\\s+");

        if (authorizationComponents.length != 2) {
            throw new InvalidTokenException();
        }

        if (!authorizationComponents[0].equals("Bearer")) {
            throw new InvalidTokenException();
        }

        return authorizationComponents[1];
    }
}