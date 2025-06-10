package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.LikeDTO;
import stay.with.me.api.service.LikeService;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 모든 좋아요 조회
    @GetMapping
    public List<LikeDTO> getAllLikes() {
        return likeService.getAllLikes();
    }

    // 특정 좋아요 조회
    @GetMapping("/{id}")
    public LikeDTO getLikeById(@PathVariable Long id) {
        return likeService.getLikeById(id);
    }

    // 좋아요 생성
    @PostMapping
    public void createLike(@RequestBody LikeDTO likeDTO) {
        likeService.createLike(likeDTO);
    }

    // 좋아요 삭제
    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
    }
}
