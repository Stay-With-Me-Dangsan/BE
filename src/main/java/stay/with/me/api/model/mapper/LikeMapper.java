package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.LikeDTO;

import java.util.List;

@Mapper
public interface LikeMapper {
    List<LikeDTO> findAll();
    LikeDTO findById(Long id);
    void insert(LikeDTO likeDTO);
    void delete(Long id);
}
