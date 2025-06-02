package stay.with.me.api.service;

import stay.with.me.api.model.dto.LikeDTO;

import java.util.List;

public interface LikeService {
    List<LikeDTO> getAllLikes();
    LikeDTO getLikeById(Long id);
    void createLike(LikeDTO likeDTO);
    void deleteLike(Long id);
}
