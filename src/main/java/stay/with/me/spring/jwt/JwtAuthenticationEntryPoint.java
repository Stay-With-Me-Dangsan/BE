package stay.with.me.spring.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//401 에러 핸들링을 위한 설정

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String exception = (String) request.getAttribute("exception");
        String message = switch (exception) {
            case "TOKEN_EXPIRED" -> "Token has expired";
            case "INVALID_TOKEN" -> "Invalid token";
            case "TOKEN_VERIFICATION_FAILED" -> "Token verification failed";
            default -> "Unauthorized request";
        };


        response.getWriter().write("{\"code\": 401, \"message\": \"" + message + "\"}");
    }
}