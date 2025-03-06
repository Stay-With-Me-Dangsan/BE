package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.CommentDTO;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDTO> findAllByPostId(Long postId);
    CommentDTO findById(Long id);
    void insert(CommentDTO commentDTO);
    void update(CommentDTO commentDTO);
    void delete(Long id);
}
