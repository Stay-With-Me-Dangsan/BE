package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardListDto;
import stay.with.me.api.model.mapper.BoardMapper;
import stay.with.me.api.model.mapper.CommonCodeMapper;
import stay.with.me.api.service.BoardService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    @Override
    public List<BoardListDto> getBoardsByUserId(Long userId) {
        return boardMapper.getBoardsByUserId(userId);
    }
}
