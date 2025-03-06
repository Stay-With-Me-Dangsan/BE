package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.CommentDTO;
import stay.with.me.api.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 특정 게시글의 모든 댓글 조회
    @GetMapping("/post/{postId}")
    public List<CommentDTO> getCommentsByPostId(@PathVariable Long postId) {
        // 특정 게시글 ID(postId)에 해당하는 모든 댓글 반환
        return commentService.getCommentsByPostId(postId);
    }

    // 특정 댓글 조회 (댓글 ID로 조회)
    @GetMapping("/{id}")
    public CommentDTO getCommentById(@PathVariable Long id) {
        // 댓글 ID를 기반으로 특정 댓글 반환
        return commentService.getCommentById(id);
    }

    // 댓글 작성
    @PostMapping
    public void createComment(@RequestBody CommentDTO commentDTO) {
        // 클라이언트에서 받은 데이터를 기반으로 댓글 생성
        commentService.createComment(commentDTO);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        // 특정 댓글 ID의 데이터를 클라이언트 요청에 따라 업데이트
        commentDTO.setCommentId(id);
        commentService.updateComment(commentDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        // 특정 댓글 ID를 기반으로 댓글 삭제
        commentService.deleteComment(id);
    }
}

