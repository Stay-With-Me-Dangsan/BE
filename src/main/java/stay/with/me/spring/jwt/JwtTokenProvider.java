package stay.with.me.spring.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * 토큰 생성, 토큰 유효성 검증
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {


    //    @Value("${jwt.secret}")
    @Value("ZGhybmZtYQoZGhybmZtYQoZGhybmZtYQoZGhybmZtYQoZGhybmZtYQo")
    private String secretKey;


    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1 hour
    private final Long refreshTokenValidMillisecond = 7 * 24 * 60 * 60 * 1000L; // 7 day
    private final CustomUserDetailsService customUserDetailsService;
    private Key key;

    // SecretKey Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * secret key 를 base64 디코드 하기위해 생성
     */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String createAccessToken(Long userId, boolean isNewUser) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("isNewUser", isNewUser);
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = getClaims(token);

        Long userId = Long.valueOf(claims.getSubject());
        UserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    // JWT 토큰 유효성 체크
    public boolean validateToken(String token, HttpServletRequest request) {
        if (token == null || token.trim().isEmpty() || token.split("\\.").length != 3) {
            log.error("잘못된 JWT 토큰이 감지됨: {}", token);
            request.setAttribute("exception", "INVALID_TOKEN");
            SecurityContextHolder.clearContext();
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다.");
            request.setAttribute("exception", "TOKEN_EXPIRED");  // 🔥 예외 메시지를 request에 저장
            SecurityContextHolder.clearContext(); // 🔥 Security Context 초기화 (인증 실패 처리)
            return false;
        } catch (MalformedJwtException e) {
            log.error("JWT 토큰 형식이 잘못되었습니다.");
            request.setAttribute("exception", "INVALID_TOKEN");
            SecurityContextHolder.clearContext();
            return false;
        } catch (Exception e) {
            log.error("JWT 검증 중 오류 발생.");
            request.setAttribute("exception", "TOKEN_VERIFICATION_FAILED");
            SecurityContextHolder.clearContext();
            return false;
        }
    }



    //    리프레시 토큰을 쿠키에 저장
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);  // ✅ JavaScript에서 접근 불가능 (보안 강화)
        cookie.setSecure(true);    // ✅ HTTPS에서만 전송
        cookie.setPath("/");       // ✅ 모든 경로에서 사용 가능
        cookie.setMaxAge(7 * 24 * 60 * 60); // ✅ 7일 동안 유지
        return cookie;
    }

    //리프레시 토큰을 쿠키에서 가져오기
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue().trim();

                    // 🔥 JWT 형식 검증
                    if (refreshToken == null || refreshToken.split("\\.").length != 3) {
                        log.error("쿠키에서 잘못된 Refresh Token이 감지됨: {}", refreshToken);
                        return null;
                    }

                    return refreshToken;
                }
            }
        }
        log.error("Refresh Token 쿠키가 존재하지 않습니다.");
        return null;
    }
    private Claims getClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getClaims(token).get("userId")).longValue();
    }

    public String getNicknameFromToken(String token) {
        return getClaims(token).getSubject();
    }
}
