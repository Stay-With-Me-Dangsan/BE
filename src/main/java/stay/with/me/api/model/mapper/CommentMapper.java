package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.CommentDTO;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDTO> findAllByPostId(Long postId);
    int insert(CommentDTO commentDTO,Long userId);
    int update(CommentDTO commentDTO,Long userId);
    int delete(Long commentId,Long userId);

    List<CommentDTO> getMyCommentsBoard(Long userId);
}
