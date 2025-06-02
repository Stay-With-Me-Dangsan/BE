package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.BoardFileDto;
import stay.with.me.api.model.mapper.BoardMapper;
import stay.with.me.api.service.BoardService;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getAllBoards(String category) {
        return boardMapper.findAll(category);
    }

    @Override
    public List<BoardDTO> getBoardsByCategory(String category, Long userId) {
        return boardMapper.findByCategory(category, userId);
    }


    @Override
    public BoardDTO getBoardById(Long postId) {
        return boardMapper.findById(postId);
    }

    @Override
    public Long createBoard(BoardDTO boardDTO) {
        boardMapper.insert(boardDTO);
        return boardDTO.getPostId();
    }

    @Override
    public void updateBoard(Long id, BoardDTO boardDTO) {
        boardDTO.setPostId(id);
        boardMapper.update(boardDTO);
    }

    @Override
    public void deleteBoard(Long id) {
        boardMapper.delete(id);
    }


    @Override
    public void createFile(BoardFileDto fileDto) throws Exception {
        boardMapper.createFile(fileDto);
    }

    @Override
    public int insetView(Long postId, Long userId) throws Exception {
        return boardMapper.insetView(postId, userId);
    }

    @Override
    public int insertLike(Long postId, Long userId) throws Exception {
        return boardMapper.insertLike(postId, userId);
    }

    @Override
    public int deleteLike(Long postId, Long userId) throws Exception {
        return boardMapper.deleteLike(postId, userId);
    }

    @Override
    public List<BoardDTO> getMyBoardUpload(Long userId) throws Exception {
        return boardMapper.getMyBoardUpload(userId);
    }
    @Override
    public List<BoardDTO> getMyLikedBoard(Long userId) throws Exception {
        return boardMapper.getMyLikedBoard(userId);
    }
}
