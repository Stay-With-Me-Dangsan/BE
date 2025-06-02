package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommentDTO;
import stay.with.me.api.model.mapper.CommentMapper;
import stay.with.me.api.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentMapper.findAllByPostId(postId);
    }

    @Override
    public int createComment(CommentDTO commentDTO, Long userId) {
        return commentMapper.insert(commentDTO ,userId);
    }

    @Override
    public int updateComment(CommentDTO commentDTO, Long userId) {
        return commentMapper.update(commentDTO,userId);
    }

    @Override
    public int deleteComment(Long postId, Long userId) {
        return commentMapper.delete(postId,userId);
    }


    @Override
    public List<CommentDTO> getMyCommentsBoard(Long userId) {
        return commentMapper.getMyCommentsBoard(userId);
    }
}
