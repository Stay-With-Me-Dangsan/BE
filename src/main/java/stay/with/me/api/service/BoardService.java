package stay.with.me.api.service;

import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.BoardFileDto;
import stay.with.me.api.model.dto.CommentDTO;
import stay.with.me.api.model.dto.HouseDetailDto;

import java.util.List;

@Service
public interface BoardService {

    List<BoardDTO> getAllBoards(String category);
    List<BoardDTO> getBoardsByCategory(String category, Long userId);
    BoardDTO getBoardById(Long postId);
    Long createBoard(BoardDTO boardDTO);
    void updateBoard(Long id, BoardDTO boardDTO);
    void deleteBoard(Long id);

    List<BoardDTO> getMyBoardUpload(Long userId) throws Exception;

    void createFile(BoardFileDto fileDto) throws Exception;

    int insetView(Long postId, Long userId) throws Exception;

    int insertLike(Long postId, Long userId) throws Exception;

    int deleteLike(Long postId, Long userId) throws Exception;

    List<BoardDTO> getMyLikedBoard(Long userId) throws Exception;
}
