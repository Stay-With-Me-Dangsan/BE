package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.LikeDTO;
import stay.with.me.api.model.mapper.LikeMapper;
import stay.with.me.api.service.LikeService;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public List<LikeDTO> getAllLikes() {
        return likeMapper.findAll();
    }

    @Override
    public LikeDTO getLikeById(Long id) {
        return likeMapper.findById(id);
    }

    @Override
    public void createLike(LikeDTO likeDTO) {
        likeMapper.insert(likeDTO);
    }

    @Override
    public void deleteLike(Long id) {
        likeMapper.delete(id);
    }
}
