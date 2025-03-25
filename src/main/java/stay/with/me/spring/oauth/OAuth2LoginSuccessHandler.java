package stay.with.me.spring.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.spring.jwt.CustomUserDetails;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            log.info("OAuth2 로그인 성공: " + authentication.getName());
            if (!(authentication.getPrincipal() instanceof CustomOAuth2User)) {
                throw new ClassCastException("OAuth2 인증 사용자 객체가 CustomOAuth2User가 아닙니다.");
            }
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = oAuth2User.getUserId();
            String provider = oAuth2User.getProvider();


            String refreshToken = jwtTokenProvider.createRefreshToken(userId);
            LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

            userMapper.updateRefreshToken(userId, refreshToken, expiredAt);
            response.addCookie(jwtTokenProvider.createRefreshTokenCookie(refreshToken));

            // 유저 정보 조회
            UserDto userDto = userMapper.findById(userId);


            // 성별이나 생일이 없으면 간편회원가입 페이지로
            String redirectUrl;
            boolean isNewUser = userDto.getBirth() == null || userDto.getGender() == null;
            String accessToken = jwtTokenProvider.createAccessToken(userId, isNewUser);

            if (isNewUser) {
                redirectUrl = "http://localhost:3000/oauth/register/" + provider + "?accessToken=" + accessToken;
            } else {
               redirectUrl = "http://localhost:3000/oauth/success/" + provider + "?accessToken=" + accessToken;
            }


            response.sendRedirect(redirectUrl);


        } catch (Exception e) {
            log.error("OAuth2 리다이렉트 실패: ", e);
        }
    }
}