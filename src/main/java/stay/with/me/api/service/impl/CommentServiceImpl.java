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
    public CommentDTO getCommentById(Long id) {
        return commentMapper.findById(id);
    }

    @Override
    public void createComment(CommentDTO commentDTO) {
        commentMapper.insert(commentDTO);
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        commentMapper.update(commentDTO);
    }

    @Override
    public void deleteComment(Long id) {
        commentMapper.delete(id);
    }
}
