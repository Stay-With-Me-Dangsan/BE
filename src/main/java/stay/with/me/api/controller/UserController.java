package stay.with.me.api.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.*;
import stay.with.me.api.model.mapper.UserMapper;
import stay.with.me.api.service.EmailService;
import stay.with.me.api.service.UserService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 - 일반 로그인
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signup( @RequestBody(required = false) UserDto userDto) {
        // 필수값 검증
        if (userDto == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            return ResponseUtil.buildResponse( ResponseStatus.BAD_REQUEST.getCode(),"필수 입력값이 누락되었습니다.",null, HttpStatus.BAD_REQUEST
            );
        }
        try {
            int signupRow = userService.signup(userDto);
            if (signupRow != 1) {
                return ResponseUtil.buildResponse( ResponseStatus.INTERNAL_ERROR.getCode(),"회원가입에 실패하였습니다. 다시 시도해주세요.",null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Map<String, Object> data = Map.of("cnt", signupRow);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(),ResponseStatus.SUCCESS.getMessage(),data,HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(),null, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto> login(@RequestBody(required = false) LoginDTO loginDto, HttpServletResponse response) {
        try {
            TokenDto tokenDto = userService.signIn(loginDto, response);

            if(tokenDto == null ){
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            response.addCookie(jwtTokenProvider.createRefreshTokenCookie(tokenDto.getRefreshToken()));

            Map<String, Object> data = Map.of("result", tokenDto);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/emailCodeSend")
    public ResponseEntity<ResponseDto> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "이메일을 입력하세요.", null, HttpStatus.BAD_REQUEST);
        }

        String code = emailService.sendVerificationCode(email);

        if (code == null) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), "이메일 전송 실패", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, Object> data = Map.of("result", code);
        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "이메일 코드 전송 완료", data, HttpStatus.OK);
    }

    @PostMapping("/emailCodeVerify")
    public ResponseEntity<ResponseDto> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "이메일과 인증 코드를 입력하세요.", null, HttpStatus.BAD_REQUEST);
        }

        boolean isValid = emailService.verifyCode(email, code);

        Map<String, Object> data = Map.of("result", isValid);
        return ResponseUtil.buildResponse(
                isValid ? ResponseStatus.SUCCESS.getCode() : ResponseStatus.BAD_REQUEST.getCode(),
                isValid ? "인증 성공" : "인증 실패",
                data,
                isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }

    @PostMapping("/findId")
    public ResponseEntity<ResponseDto> findId(@RequestBody UserDto userDto) {
        try {
            UserDto user =userService.findEmail(userDto);
            if (user == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), "등록된 이메일이 없습니다.", null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", user);
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/findPw")
    public ResponseEntity<ResponseDto> findPassword(@RequestParam String email){
        boolean isSent = userService.sendTemporaryPassword(email);
        if (!isSent) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "등록된 이메일이 없습니다.", null, HttpStatus.BAD_REQUEST);
        }
        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "임시 비밀번호가 이메일로 전송되었습니다.", null, HttpStatus.OK);

    }
    // 닉네임 변경
    @PatchMapping("/updateNickname")
    public ResponseEntity<ResponseDto> updateNickname(@RequestBody UserDto userDto) {

        int createdRow = userService.updateNickname(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "닉네임 변경 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "닉네임 변경 성공", null, HttpStatus.OK);
    }
    // 프로필변경
    @PatchMapping("/updateMypage")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody UserDto userDto) {

        int createdRow = userService.updateMypage(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "프로필 수정 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "프로필 수정 성공", null, HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) throws Exception {
        try {
            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }

            Long userId = userDetails.getUserId();
            userService.logoutUser(userId, response); // ✅ 리프레시 토큰 삭제 및 로그아웃 처리

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "로그아웃 성공", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //회원탈퇴 ( 계정 삭제 )
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {
        try {
            if (!userId.equals(userDetails.getUserId())) {
                return ResponseUtil.buildResponse(ResponseStatus.FORBIDDEN.getCode(), "권한이 없습니다.", null, HttpStatus.FORBIDDEN);
            }

            userService.logoutUser(userId, response); // ✅ 리프레시 토큰 삭제 및 로그아웃 처리
            userService.deleteUser(userId);  // ✅ DB에서 유저 삭제

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "계정이 성공적으로 삭제되었습니다.", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}


