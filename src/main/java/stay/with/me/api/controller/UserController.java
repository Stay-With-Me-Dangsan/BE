package stay.with.me.api.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.model.dto.user.LoginDTO;
import stay.with.me.api.model.dto.user.TokenDto;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.service.EmailService;
import stay.with.me.api.service.UserService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;
import stay.with.me.spring.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signup(@RequestBody(required = false) UserDto userDto) {
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

    @PatchMapping("/oauthReg")
    public ResponseEntity<ResponseDto> OuathReg(@RequestBody UserDto userDto) {

        int createdRow = userService.updateOuathReg(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "간편가입실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "간편가입성공", null, HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto> login(@RequestBody(required = false) LoginDTO loginDto, HttpServletResponse response) {
        try {
            TokenDto tokenDto = userService.signIn(loginDto, response);

            if(tokenDto == null ){
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }


            Map<String, Object> data = Map.of("result", tokenDto);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
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

    @PostMapping("/findEmail")
    public ResponseEntity<ResponseDto> findEmail(@RequestBody UserInfoDto userInfoDto) {
        try {
            UserInfoDto user =userService.findEmail(userInfoDto);
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
    public ResponseEntity<ResponseDto> findPassword(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        boolean isSent = userService.sendTemporaryPassword(email);
        if (!isSent) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "등록된 이메일이 없습니다.", null, HttpStatus.BAD_REQUEST);
        }
        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "임시 비밀번호가 이메일로 전송되었습니다.", null, HttpStatus.OK);

    }
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            String refreshToken = jwtTokenProvider.getRefreshTokenFromCookie(request);

            if (refreshToken == null ) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(),
                        "Refresh Token이 존재하지 않습니다.", null, HttpStatus.UNAUTHORIZED);
            }
            TokenDto tokenDto = userService.refreshAccessToken(refreshToken, response);

            Map<String, Object> data = Map.of("accessToken", tokenDto.getAccessToken());

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK );

        } catch (IllegalArgumentException e) { // 401 에러
            return ResponseUtil.buildResponse(  ResponseStatus.UNAUTHORIZED.getCode(), e.getMessage(), null, HttpStatus.UNAUTHORIZED );
        } catch (Exception e) { //500 에러
            return ResponseUtil.buildResponse( ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(),null, HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }


}


