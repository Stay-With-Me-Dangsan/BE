package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.CommentDTO;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.service.CommentService;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 특정 게시글의 모든 댓글 조회
    @GetMapping("/get/{postId}")
    public ResponseEntity<ResponseDto> getCommentsByPostId(@PathVariable Long postId) {

        try {

            List<CommentDTO> commentList = commentService.getCommentsByPostId(postId);

            Map<String, Object> data = Map.of("result", commentList);

            return ResponseUtil.buildResponse(stay.with.me.common.ResponseStatus.SUCCESS.getCode(), stay.with.me.common.ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(stay.with.me.common.ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 댓글 작성
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createComment(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {

            Long userId = userDetails.getUserId();
            int createdRow = commentService.createComment(commentDTO, userId);

            Map<String, Object> data = Map.of("result", createdRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 수정
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateComment(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            Long userId = userDetails.getUserId();
            int updatedRow = commentService.updateComment(commentDTO, userId);

            Map<String, Object> data = Map.of("result", updatedRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            Long userId = userDetails.getUserId();
            int deletedRow = commentService.deleteComment(commentId, userId);
            Map<String, Object> data = Map.of("result", deletedRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}