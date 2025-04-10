package stay.with.me.spring.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.api.service.TemporalUtil;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserMapper userMapper;
    private final TemporalUtil temporalUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest); // OAuth2 사용자 정보 로드

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex) kakao, google, naver
        Map<String, Object> attributes = oauth2User.getAttributes();
        String providerId = null;
        String email = null;

        if ("google".equals(provider)) {
            System.out.println("Google OAuth2 Attributes: " + attributes);
            providerId = (String) attributes.get("id");
            email = (String) attributes.get("email");
        } else if ("kakao".equals(provider)) {
            providerId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");
            }
        } else if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response != null) {
                providerId = (String) response.get("id");
                email = (String) response.get("email");
            }
        } else {
            throw new OAuth2AuthenticationException("제공받지못한 provider: " + provider);
        }

        if (providerId == null) {
            throw new OAuth2AuthenticationException("Failed " + provider);
        }


        UserDto existingUser = userMapper.findByEmail(email);
        Long userId;
        String nickname = null;
        String gender = null;
        String birth = null;
        if(existingUser == null) {

            String password = temporalUtil.socialPassword(provider);
            UserDto newUser = new UserDto();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setNickname(nickname);
            newUser.setBirth(birth);
            newUser.setGender(gender);
            newUser.setProvider(provider);

            userMapper.signUp(newUser);

            userId = newUser.getUserId();

        } else {
            userId = existingUser.getUserId();
            nickname = existingUser.getNickname();
            gender = existingUser.getGender();
            birth = existingUser.getBirth();
        }

        boolean isAlreadyOauth = userMapper.existsOauthUser(provider, providerId);

        if (!isAlreadyOauth) {
            userMapper.InsertOauth(new UserDto(
                    userId, email, provider, providerId, gender, nickname, birth));
        }

        userMapper.updateLastLogin(userId, provider);

        return new CustomOAuth2User(oauth2User, provider, userId);
    }
}