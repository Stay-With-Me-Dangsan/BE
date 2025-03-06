package stay.with.me.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import stay.with.me.api.service.BoardService;
import stay.with.me.api.model.dto.BoardDTO;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    //모든 게시글 조회
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        //서비스 계층을 호출해서 모든 게시글 데이터 가져옴.
        return boardService.getAllBoards();
    }

    //게시글 생성
    @PostMapping
    public void createBoard(@RequestBody BoardDTO boardDTO) {
        // 클라이언트가 전달한 게시글 데이터 저장.
        boardService.createBoard(boardDTO);
    }

    //특정 게시글 조회(게시글 ID 참조해서 조회함)
    @GetMapping("/{id}")
    public BoardDTO getBoardById(@PathVariable Long id) {
        //게시글 ID 참조해서 특정 게시글 데이터를 가져옴.
        return boardService.getBoardById(id);
    }

    //게시글 수정
    @PutMapping("/{id}")
    public void updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        // 특정 ID의 게시글 데이터 업데이트.
        boardService.updateBoard(id, boardDTO);
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        // 특정 ID의 게시글 데이터를 삭제.
        boardService.deleteBoard(id);
    }
}

