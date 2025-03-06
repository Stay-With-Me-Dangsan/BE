package stay.with.me.api.service;

import stay.with.me.api.model.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByPostId(Long postId);
    CommentDTO getCommentById(Long id);
    void createComment(CommentDTO commentDTO);
    void updateComment(CommentDTO commentDTO);
    void deleteComment(Long id);
}
