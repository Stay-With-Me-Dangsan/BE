package stay.with.me.api.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.CommentDTO;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.service.BoardService;
import stay.with.me.api.service.CommentService;
import stay.with.me.api.service.HouseService;
import stay.with.me.api.service.UserService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final UserService userService;
    private final HouseService houseService;
    private final BoardService boardService;
    private final CommentService commentService;

//    마이페이지
    @GetMapping("/{userId}")
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
            Map<String, Object> data = Map.of("result", userDto);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(),"사용자 정보 조회 성공", data, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(),
                    e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

//내가 올린 게시판
    @GetMapping("/board/upload")
    public ResponseEntity<ResponseDto> getMyUploadedBoard(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {


            Long userId = userDetails.getUserId();

            List<BoardDTO> boardList = boardService.getMyBoardUpload(userId);
            Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // 게시판 좋아요 추가
    @PostMapping("/board/bookmark/{houseDetailId}")
    public ResponseEntity<ResponseDto> bookmarkHouse(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable int houseDetailId) {

        Long userId = userDetails.getUserId();

        int createdRow = houseService.plusBookMart(userId, houseDetailId);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "찜하기 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "찜하기 성공", null, HttpStatus.OK);

    }

    // 게시판 좋아요 취소
    @DeleteMapping("/board/like/cancle/{boardDetailId}")
    public ResponseEntity<ResponseDto> cancelLike(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable int houseDetailId) {

        Long userId = userDetails.getUserId();

        int createdRow = houseService.minusBookMart(userId, houseDetailId);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "찜하기 취소 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "찜하기 취소 성공", null, HttpStatus.OK);

    }

// 내가 작성한 댓글
    @GetMapping("/board/comments")
    public ResponseEntity<ResponseDto> getBoardRecomment(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            Long userId = userDetails.getUserId();
            List<CommentDTO> commentsList = commentService.getMyCommentsBoard(userId);

            Map<String, Object> data = Map.of("result", commentsList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

// 내가 좋아요 한 게시글
    @GetMapping("/board/like")
    public ResponseEntity<ResponseDto> getMyLikedBoard( @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {


            Long userId = userDetails.getUserId();

            List<BoardDTO> boardList = boardService.getMyLikedBoard(userId);

            Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


//내가 올린 하우스
    @GetMapping("/house/upload")
    public ResponseEntity<ResponseDto> getMyUploadedHouses(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }
            Long userId = userDetails.getUserId();

            List<HouseDetailDto> houseList = houseService.getHousesByUserId(userId);
            Map<String, Object> data = Map.of("result", houseList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


//내가 좋아요한 하우스
    @GetMapping("/house/bookmark")
    public ResponseEntity<ResponseDto> getBookmarkedHouse(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }
            Long userId = userDetails.getUserId();
            List<HouseDetailDto> houseList = houseService.getMarkedHouse(userId);
            Map<String, Object> data = Map.of("result", houseList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


// 하우스 좋아요 추가
    @PostMapping("/house/bookmark/{houseDetailId}")
    public ResponseEntity<ResponseDto> houseBookmarkHouse(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable int houseDetailId) {

        Long userId = userDetails.getUserId();

        int createdRow = houseService.plusBookMart(userId, houseDetailId);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "찜하기 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "찜하기 성공", null, HttpStatus.OK);

    }

    // 찜 취소
    @DeleteMapping("/house/bookmark/cancle/{houseDetailId}")
    public ResponseEntity<ResponseDto> houseBookmarkCancle(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable int houseDetailId) {

        Long userId = userDetails.getUserId();

        int createdRow = houseService.minusBookMart(userId, houseDetailId);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "찜하기 취소 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "찜하기 취소 성공", null, HttpStatus.OK);

    }

    @GetMapping("/house/view")
    public ResponseEntity<ResponseDto> getRecentView(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        try {

            if (userDetails == null) {
                return ResponseUtil.buildResponse(ResponseStatus.UNAUTHORIZED.getCode(), "로그인이 필요합니다.", null, HttpStatus.UNAUTHORIZED);
            }

            Long userId = userDetails.getUserId();
            List<HouseDetailDto> houseList = houseService.getRecentView(userId);

            Map<String, Object> data = Map.of("result", houseList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PatchMapping("/update/nickname")
    public ResponseEntity<ResponseDto> updateNickname(@RequestBody UserDto userDto) {

        int createdRow = userService.updateNickname(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "닉네임 변경 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "닉네임 변경 성공", null, HttpStatus.OK);
    }

    @PatchMapping("/update/email")
    public ResponseEntity<ResponseDto> updateEmail(@RequestBody UserDto userDto) throws Exception {

        int createdRow = userService.updateEmail(userDto);

        if (createdRow != 1) {
            return ResponseUtil.buildResponse(ResponseStatus.BAD_REQUEST.getCode(), "프로필 수정 실패", null, HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), "프로필 수정 성공", null, HttpStatus.OK);
    }


    @PatchMapping("/update/pw")
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

}


