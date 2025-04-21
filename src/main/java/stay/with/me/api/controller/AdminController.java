package stay.with.me.api.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.BoardListDto;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.model.dto.user.LoginDTO;
import stay.with.me.api.model.dto.user.TokenDto;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.service.AdminService;
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
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/user/list")
    public ResponseEntity<ResponseDto> getUserList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }

            List<UserInfoDto> userList = adminService.getUserList();
            Map<String, Object> data = Map.of("result", userList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @GetMapping("/board/list")
    public ResponseEntity<ResponseDto> getBoardList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }

            List<BoardListDto> boardList = adminService.getBoardList();
            Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}


