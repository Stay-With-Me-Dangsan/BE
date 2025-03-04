package stay.with.me.api.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import stay.with.me.api.model.dto.LoginDTO;
import stay.with.me.api.model.dto.TokenDto;
import stay.with.me.api.model.dto.UserDto;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.api.service.EmailService;
import stay.with.me.api.service.UserService;
import stay.with.me.spring.jwt.CustomUserDetails;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    //    회원가입
    @Override
    public int signup(UserDto userDto) throws Exception {

        UserDto existingUser = userMapper.findByEmail(userDto.getEmail()); // 🔹 기존 회원 정보를 가져옴
        if (existingUser != null) {  throw new DuplicateMemberException("이미 존재하는 이메일입니다.");}
        String encodedPw = bCryptPasswordEncoder.encode(userDto.getPassword()); //비밀번호를 암호화
        userDto.setPassword(encodedPw);
        return userMapper.signUp(userDto);
    }

    //   일반 로그인
    @Override
    public TokenDto signIn(LoginDTO loginDto, HttpServletResponse response) {

        UserDto userDto = Optional.ofNullable(userMapper.findByEmail(loginDto.getEmail()))
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), userDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 다시 확인해주세요.");
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        String accessToken = jwtTokenProvider.createAccessToken(userDto.getEmail(), userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto.getEmail(), userId);


        // refreshToken을 DB에 업데이트
        if (userMapper.SaveOrUpdateRefreshToken(userId, refreshToken) <= 0) {
            throw new RuntimeException("Refresh Token 업데이트 실패");
        }

        return new TokenDto(accessToken, userDto);
    }
    // 회원탈퇴(계정 삭제)
    @Override
    public void deleteUser(Long userId) throws Exception {  userMapper.deleteUser(userId); }

    public int updateNickname(UserDto userDto) {
      return userMapper.updateNickname(userDto);
    }

    public int updateMypage(UserDto userDto) {
       return userMapper.updateMypage(userDto);
    }

    @Override
    public UserDto getUserById(Long userId) {return userMapper.findById(userId); }

    @Override
    public String findEmail(String nickname, String birth) { return userMapper.findEmail(nickname, birth); }


    @Override
    public boolean sendTemporaryPassword(String email) {
        UserDto user = userMapper.findByEmail(email);
        if (user == null) {return false; }
        // 임시 비밀번호 생성 (8자리 랜덤 UUID)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        // 비밀번호 업데이트
        userMapper.updateTempPassword(email, tempPassword);
        // 이메일 발송
        emailService.sendEmail(email, "임시 비밀번호 발급", "임시 비밀번호: " + tempPassword);
        return true;
    }



    // 로그아웃 및 리프레시 토큰 삭제 처리
    public void logoutUser(Long userId, HttpServletResponse response) {
        userMapper.deleteRefreshToken(userId);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}