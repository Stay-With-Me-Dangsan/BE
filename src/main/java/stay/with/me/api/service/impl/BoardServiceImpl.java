package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.mapper.BoardMapper;
import stay.with.me.api.service.BoardService;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getAllBoards() {
        return boardMapper.findAll();
    }

    @Override
    public BoardDTO getBoardById(Long id) {
        return boardMapper.findById(id);
    }

    @Override
    public void createBoard(BoardDTO boardDTO) {
        boardMapper.insert(boardDTO);
    }

    @Override
    public void updateBoard(Long id, BoardDTO boardDTO) {
        boardMapper.update(id, boardDTO);
    }

    @Override
    public void deleteBoard(Long id) {
        boardMapper.delete(id);
    }
}
