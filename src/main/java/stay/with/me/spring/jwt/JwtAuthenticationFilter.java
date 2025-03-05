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

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Access Token 추출
        String accessToken = resolveToken(request);
        // 🔹 Authorization 헤더가 없는 경우, 인증을 시도하지 않음 (예외 방지)
        if (accessToken == null || accessToken.isEmpty()) {
            log.warn("Authorization 헤더가 없음. 인증 없이 API를 실행합니다.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtTokenProvider.validateToken(accessToken, request)) {
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.error("JWT 토큰이 만료됨: " + accessToken);
            }
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 형식: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료됨");
        }

//
//        if (accessToken != null && jwtTokenProvider.validateToken(accessToken, request)) {
//            // Access Token이 유효한 경우 인증 정보 설정
//            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        } else if (accessToken != null) {
//            // Access Token이 만료된 경우 Refresh Token 확인
//            String refreshToken = jwtTokenProvider.getRefreshTokenFromCookie(request);
//
//            if (refreshToken == null) {
//                log.error("Refresh Token이 쿠키에서 조회되지 않음");
//                request.setAttribute("exception", "NO_REFRESH_TOKEN");
//                SecurityContextHolder.clearContext();
//            } else if (!jwtTokenProvider.validateToken(refreshToken, request)) {
//                log.error("Refresh Token이 유효하지 않음: " + refreshToken);
//                request.setAttribute("exception", "EXPIRED_REFRESH_TOKEN");
//                SecurityContextHolder.clearContext();
//            } else {
//                String email = jwtTokenProvider.getEmailFromToken(refreshToken);
//                Long userId = jwtTokenProvider.getUserIdFromToken(email);
//                String newAccessToken = jwtTokenProvider.createAccessToken(email, userId);
//                log.info("새로운 Access Token 발급: " + newAccessToken);
//                response.setHeader("Authorization", "Bearer " + newAccessToken);
//
//                Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }

        filterChain.doFilter(request, response);
    }


    // HTTP Request 헤더로부터 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || bearerToken.trim().isEmpty()) {
            log.error("Authorization 헤더가 없습니다.");
            return null;
        }

        if (!bearerToken.startsWith("Bearer ")) {
            log.error("Bearer 형식이 아닌 Authorization 헤더가 감지됨: {}", bearerToken);
            return null;
        }

        String token = bearerToken.substring(7);

        // 🔥 JWT 형식 검증 (Header.Payload.Signature)
        if (token.split("\\.").length != 3) {
            log.error("잘못된 JWT 토큰 형식입니다: {}", token);
            return null;
        }

        return token;
    }
}
