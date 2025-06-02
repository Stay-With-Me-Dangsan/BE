package stay.with.me.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import stay.with.me.api.model.dto.CommonCodeDto;
import stay.with.me.api.model.dto.ResponseDto;
import stay.with.me.api.service.BoardService;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.common.ResponseStatus;
import stay.with.me.common.util.ResponseUtil;
import stay.with.me.spring.jwt.CustomUserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // category로 게시글 목록 조회

    @GetMapping("/category")
    public ResponseEntity<ResponseDto> getBoardsByCategory(@RequestParam(required = false) String category,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            //            Long userId = userDetails.getUserId();
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
                  List<BoardDTO> boardList;

                if (category != null && !category.isEmpty()) {
                     boardList = boardService.getBoardsByCategory(category, userId);
                } else {
                     boardList = boardService.getAllBoards(category);
                }

                Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //특정 게시글 조회(게시글 ID 참조해서 조회함)
    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDto> getBoardById(@PathVariable Long postId) {
        try {
            BoardDTO boardList =  boardService.getBoardById(postId);

            if(boardList == null) {
                return ResponseUtil.buildResponse(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage(), null, HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = Map.of("result", boardList);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //게시글 생성
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createBoard(@RequestBody BoardDTO boardDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

//            Long userId = userDetails.getUserId();
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;

            Long postId = boardService.createBoard(boardDTO);

            Map<String, Object> data = Collections.singletonMap("result", postId);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/update/view")
    public ResponseEntity<ResponseDto> updateView(@RequestBody Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            //            Long userId = userDetails.getUserId();
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;


            int insertedRow = boardService.insetView(postId, userId);

            Map<String, Object> data = Map.of("result", insertedRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insert/like")
    public ResponseEntity<ResponseDto> updateLike(@RequestBody Long postId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            //            Long userId = userDetails.getUserId();
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
            int updatedRow = boardService.insertLike(postId, userId);

            Map<String, Object> data = Map.of("result", updatedRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/like")
    public ResponseEntity<ResponseDto> deleteLike(@RequestParam(required = false) Long postId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {

            //            Long userId = userDetails.getUserId();
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
            int updatedRow = boardService.deleteLike(postId, userId);
            Map<String, Object> data = Map.of("result", updatedRow);

            return ResponseUtil.buildResponse(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.buildResponse(ResponseStatus.INTERNAL_ERROR.getCode(), ResponseStatus.INTERNAL_ERROR.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

