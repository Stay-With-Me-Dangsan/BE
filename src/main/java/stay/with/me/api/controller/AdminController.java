package stay.with.me.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.service.AdminService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/user/list")
    public ResponseEntity<ResponseDto> getAdminUserList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }

            List<UserInfoDto> userList = adminService.getAdminUserList();
            Map<String, Object> data = Map.of("result", userList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @GetMapping("/board/list")
    public ResponseEntity<ResponseDto> getAdminBoardList(@AuthenticationPrincipal CustomUserDetails userDetails
             ,@RequestParam(value = "category", required = false) String category) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }

            List<BoardDTO> boardList = adminService.getAdminBoardList(category);

            Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/board/blind/update")
    public ResponseEntity<ResponseDto> updateBoardBlind( @RequestBody List<Long> selectedIds ) {
        try {
            int updatedRow = adminService.updateBoardBlind(selectedIds);
            Map<String, Object> data = Map.of("cnt", updatedRow);
            if (updatedRow != 1) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), data, HttpStatus.NOT_FOUND);
            }
            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


