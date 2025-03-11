package stay.with.me.spring.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.spring.jwt.CustomUserDetails;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public OAuth2LoginSuccessHandler(JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("인증된 사용자 정보: " + userDetails);

        Long userId = userDetails.getUserId();



        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        int rowsAffected = userMapper.SaveOrUpdateRefreshToken(userId, refreshToken);
        if (rowsAffected > 0) {
            System.out.println("Refresh Token 업데이트 성공");
        } else {
            System.out.println("Refresh Token 업데이트 실패");
        }

        // 🔹 JWT를 응답 헤더에 추가
        response.setHeader("Authorization", "Bearer " + accessToken);

        // 🔹 로그인 성공 후 리다이렉트 (필요 시)
        response.sendRedirect("/home");  // 예시: 로그인 후 홈 페이지로 리다이렉트
    }
}


