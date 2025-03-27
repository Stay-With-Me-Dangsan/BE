package stay.with.me.spring.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import stay.with.me.common.ResponseStatus;

import java.io.IOException;
import java.io.PrintWriter;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//검증과 인증을 처리하는 곳

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);

        if (accessToken == null || accessToken.isEmpty() || !accessToken.startsWith("Bearer")) {
           filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtTokenProvider.validateToken(accessToken, request)) {
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.error("JWT 토큰이 유효하지 않음");

            }
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료됨: {}", e.getMessage());
            sendJsonResponse(response, ResponseStatus.UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 형식: {}", e.getMessage());
            sendJsonResponse(response, ResponseStatus.BAD_REQUEST);
            return;
        }


        filterChain.doFilter(request, response);
    }



    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return token;
        }
        return null;
    }

    private void sendJsonResponse(HttpServletResponse response, ResponseStatus responseStatus) throws IOException {
        response.setStatus(responseStatus.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("{ \"status\": " + responseStatus.getCode() + ", \"message\": \"" + responseStatus.getMessage() + "\" }");
        out.flush();
    }
}
