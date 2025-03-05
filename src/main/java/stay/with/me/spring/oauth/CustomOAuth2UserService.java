package stay.with.me.spring.oauth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import stay.with.me.api.model.dto.UserDto;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest); // OAuth2 사용자 정보 로드
        System.out.println("OAuth2UserServiceImpl 도착 : " + oauth2User);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex) kakao, google, naver
        System.out.println("provider " + provider);

        // ✅ 요청에서 userId 가져오기 (마이페이지에서 전달한 값)
        Long userId = null;
        Object userIdObj = userRequest.getAdditionalParameters().get("userId");
        if (userIdObj != null) {
            try {
                userId = Long.valueOf(userIdObj.toString()); // String을 Long으로 변환
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        String providerId = null;
        String email = null;
        String state = null;

        // 각 provider에 맞게 이메일과 ID 추출
        if ("kakao".equals(provider)) {
            System.out.println("kakao ");
            // ✅ Kakao 사용자 정보 파싱
            Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttribute("kakao_account");
            email = (String) kakaoAccount.get("email");
//            profileImage = (String) kakaoAccount.get("profile_image_url");
            Object kakaoId = oauth2User.getAttribute("id"); // providerId = String.valueOf(oauth2User.getAttribute("id"));

            if (kakaoId instanceof Long) {
                providerId = String.valueOf(kakaoId); // Long을 String으로 변환
            } else {
                providerId = (String) kakaoId; // 이미 String인 경우
            }


            System.out.println("카카오 ID: " + providerId);
            System.out.println("카카오 이메일: " + email);
        }else if ("naver".equals(provider)) {
            System.out.println("naver ");
            Map<String, Object> response = oauth2User.getAttribute("response");
            email = (String) response.get("email");
            providerId = (String) response.get("id");
            state = (String) userRequest.getAdditionalParameters().get("state");
//            profileImage = (String) response.get("profile_image");
            System.out.println("naver ID: " + providerId);
            System.out.println("naver 이메일: " + email);

        }else if ("google".equals(provider)) {
            System.out.println("google ");
            providerId = oauth2User.getAttribute("sub"); // Google의 경우 "sub", Kakao는 "id", Naver는 "response.id"
            email = oauth2User.getAttribute("email");
//            profileImage = oauth2User.getAttribute("picture");
            System.out.println("naver ID: " + providerId);
            System.out.println("naver 이메일: " + email);
        }

        // ✅ userId가 있으면 기존 계정과 소셜 계정을 연결
        UserDto existingUser = null;

        if (userId != null) {
            existingUser = userMapper.findById(userId);
        } else {
            existingUser = userMapper.findByEmail(email);
        }

        System.out.println(" ID: " + providerId);
        System.out.println(" email: " + email);

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createAccessToken(email, userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(email, userId);

        if(existingUser != null) {
            // ✅ 기존 계정이 있으면 provider, providerId 업데이트 (소셜 로그인 연동)
            userMapper.updateSocialLogin(existingUser.getUserId(), provider, providerId);
        } else {
            // 신규 사용자 등록
            UserDto user = new UserDto(email, provider, providerId, refreshToken);
            userMapper.OauthSingUp(user);
        }


        // 리디렉션 URL
        String redirectUrl;
        System.out.println("OAuth2UserServiceImpl 도착 : " + jwtToken);

        if ("naver".equals(provider)) {
            redirectUrl = "http://localhost:3000/login/oauth2/callback/naver?token=" + jwtToken + "&state=" + state;
        } else if ("kakao".equals(provider)) {
            redirectUrl = "http://localhost:3000/login/oauth2/callback/kakao?token=" + jwtToken;
        } else {
            redirectUrl = "http://localhost:3000/login/oauth2/callback/google?token=" + jwtToken;
        }
        // 리디렉션 URL
        System.out.println("redirect_Url : " + redirectUrl);
        // 리디렉트 응답
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            if (response != null) {
                response.setStatus(HttpServletResponse.SC_FOUND);
                response.setHeader("Location", redirectUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("OAuth2 로그인 후 리디렉트 중 오류 발생", e);
        }

        // CustomOAuth2User 반환
        return new CustomOAuth2User(oauth2User); // OAuth2User 반환
    }
}