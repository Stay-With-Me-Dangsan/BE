package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.BoardListDto;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardListDto> getBoardsByUserId(Long userId);
}
