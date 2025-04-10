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

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
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


            Map<String, Object> data = Map.of("user", tokenDto);

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

        Map<String, Object> data = Map.of("user", code);
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

        Map<String, Object> data = Map.of("user", isValid);
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
            Map<String, Object> data = Map.of("user", user);
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
    @GetMapping("/mypage/{userId}")
    public ResponseEntity<ResponseDto> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(),"로그인이 필요합니다.", null, HttpStatus.NOT_FOUND);
            }

            Long userId = userDetails.getUserId();
            UserDto userDto = userService.getUserById(userId);

            if(userDto == null ){
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("user", userDto);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(),"사용자 정보 조회 성공", data, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(),
                    e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/mypage/updateNickname")
    public ResponseEntity<ResponseDto> updateNickname(@RequestBody UserDto userDto) {

        int createdRow = userService.updateNickname(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "닉네임 변경 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "닉네임 변경 성공", null, HttpStatus.OK);
    }

    @PatchMapping("/mypage/updateEmail")
    public ResponseEntity<ResponseDto> updateEmail(@RequestBody UserDto userDto) throws Exception {

        int createdRow = userService.updateEmail(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "프로필 수정 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "프로필 수정 성공", null, HttpStatus.OK);
    }


    @PatchMapping("/mypage/updatePw")
    public ResponseEntity<ResponseDto> updatePw(@RequestBody UserDto userDto) {

        int createdRow = userService.updatePw(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "프로필 수정 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "프로필 수정 성공", null, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) throws Exception {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }
            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "인증 정보가 없습니다.", null, HttpStatus.UNAUTHORIZED);
            }

            Long userId = userDetails.getUserId();
            if (userId == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), "사용자를 찾을 수 없습니다.", null, HttpStatus.NOT_FOUND);
            }
            try {
            userService.logoutUser(userId, response);
            } finally {
                SecurityContextHolder.clearContext();
            }

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "로그아웃 성공", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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


