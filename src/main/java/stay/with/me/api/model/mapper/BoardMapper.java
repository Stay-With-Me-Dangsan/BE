package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.BoardFileDto;
import stay.with.me.api.model.dto.CommentDTO;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDTO> findAll(String category);
    List<BoardDTO> findByCategory(String category, Long userId);
    BoardDTO findById(Long postId);
    void insert(BoardDTO boardDTO);
    void update(BoardDTO boardDTO);
    void delete(Long id);
    void createFile(BoardFileDto fileDto);

    int insetView(Long postId, Long userId);

    int insertLike(Long postId, Long userId);

    int deleteLike(Long postId, Long userId);

    List<BoardDTO> getMyBoardUpload(Long userId);

    List<BoardDTO> getMyLikedBoard(Long userId);

}
