package stay.with.me.api.service;

import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardDTO;

import java.util.List;

@Service
public interface BoardService {
    List<BoardDTO> getAllBoards();
    BoardDTO getBoardById(Long id);
    void createBoard(BoardDTO boardDTO);
    void updateBoard(Long id, BoardDTO boardDTO);
    void deleteBoard(Long id);
}
