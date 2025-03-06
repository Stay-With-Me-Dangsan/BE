package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.BoardDTO;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDTO> findAll();
    BoardDTO findById(Long id);
    void insert(BoardDTO boardDTO);
    void update(Long id, BoardDTO boardDTO);
    void delete(Long id);
}
