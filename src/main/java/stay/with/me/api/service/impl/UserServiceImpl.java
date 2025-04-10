package stay.with.me.api.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.user.LoginDTO;
import stay.with.me.api.model.dto.user.TokenDto;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.api.service.EmailService;
import stay.with.me.api.service.TemporalUtil;
import stay.with.me.api.service.UserService;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final TemporalUtil temporalUtil;

    @Override
    public int signup(UserDto userDto) throws Exception {

        UserDto existingUser = userMapper.findByEmail(userDto.getEmail());
        if (existingUser != null) {  throw new DuplicateMemberException("이미 존재하는 이메일입니다.");}
        String encodedPw = bCryptPasswordEncoder.encode(userDto.getPassword()); //비밀번호를 암호화
        userDto.setPassword(encodedPw);
        return userMapper.signUp(userDto);
    }

    @Override
    public TokenDto signIn(LoginDTO loginDto, HttpServletResponse response) {

        UserDto userDto = Optional.ofNullable(userMapper.findByEmail(loginDto.getEmail()))
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), userDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해주세요.");
        }

        // Spring Security 인증 처리
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        //일반회원의 경우 gender, birth 무조건 기입
        boolean isNewUser = false;

        String accessToken = jwtTokenProvider.createAccessToken(userDto.getUserId(), isNewUser, userDto.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto.getUserId());
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        userMapper.updateLastLogin(userDto.getUserId(), "local");

        userMapper.updateRefreshToken(userDto.getUserId(), refreshToken, expiredAt);

        response.addCookie(jwtTokenProvider.createRefreshTokenCookie(refreshToken));

        return new TokenDto(accessToken, userDto.getUserId());
    }


    @Override
    public UserDto getUserById(Long userId) {
        return Optional.ofNullable(userMapper.findById(userId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public int updateOuathReg(UserDto userDto) {
        return userMapper.updateOauthReg(userDto);
    }

    @Override
    public TokenDto refreshAccessToken(String refreshToken, HttpServletResponse response) {

        if (!jwtTokenProvider.validateToken(refreshToken, null)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        UserDto userDto = Optional.ofNullable(userMapper.findByEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String storedRefreshToken = userMapper.findRefreshTokenByUserId(userDto.getUserId());
        log.info("storedRefreshToken : {}" + storedRefreshToken);


        if (!refreshToken.equals(storedRefreshToken)) {
            log.info("DB의 Refresh Token과 일치하지 않습니다.");
            throw new IllegalArgumentException("DB의 Refresh Token과 일치하지 않습니다.");
        }

        boolean isNewUser = false;

        String newAccessToken = jwtTokenProvider.createAccessToken(userDto.getUserId(), isNewUser, userDto.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userDto.getUserId());


        if (!storedRefreshToken.equals(newRefreshToken)) {
            LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
            userMapper.updateRefreshToken(userDto.getUserId(), newRefreshToken, expiredAt);
            response.addCookie(jwtTokenProvider.createRefreshTokenCookie(newRefreshToken));
        }

        return new TokenDto(newAccessToken, userDto.getUserId());
    }



    @Override
    public void deleteUser(Long userId)  {  userMapper.deleteUser(userId); }
    @Override
    public int updateNickname(UserDto userDto) {
        return userMapper.updateNickname(userDto);
    }

    @Override
    public int updateEmail(UserDto userDto) throws Exception {

        UserDto existingUser = userMapper.findByEmail(userDto.getEmail());
        if (existingUser != null) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }


        return userMapper.updateEmail(userDto);
    }

    @Override
    public int updatePw(UserDto userDto) {

        String encodedPw = bCryptPasswordEncoder.encode(userDto.getPassword()); //비밀번호를 암호화
        userDto.setPassword(encodedPw);

        return userMapper.updatePw(userDto);
    }
    @Override
    public UserInfoDto findEmail(UserInfoDto userInfoDto) { return userMapper.findEmail(userInfoDto); }


    @Override
    public boolean sendTemporaryPassword(String email) {

        UserDto user = userMapper.findByEmail(email);
        if (user == null) {return false; }

        String tempPassword = temporalUtil.generateTempPassword();

        String encodePassword = bCryptPasswordEncoder.encode(tempPassword);
        userMapper.updateTempPassword(email, encodePassword);

        emailService.sendEmail(email, "StayWithMe 임시 비밀번호 발급", "임시 비밀번호는 " + tempPassword +"입니다 :)");
        return true;
    }



    // 로그아웃 및 리프레시 토큰 삭제 처리
    public void logoutUser(Long userId, HttpServletResponse response) {
        userMapper.deleteRefreshToken(userId);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}