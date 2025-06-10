package stay.with.me.api.service;

import stay.with.me.api.model.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByPostId(Long postId);
    int createComment(CommentDTO commentDTO, Long userId);
    int updateComment(CommentDTO commentDTO, Long userId);
    int deleteComment(Long commentId, Long userId);

    List<CommentDTO> getMyCommentsBoard(Long userId);
}
