package stay.with.me.api.service;

import stay.with.me.api.model.dto.BoardListDto;

import java.util.List;

public interface BoardService {
    List<BoardListDto> getBoardsByUserId(Long userId) throws Exception;
}
