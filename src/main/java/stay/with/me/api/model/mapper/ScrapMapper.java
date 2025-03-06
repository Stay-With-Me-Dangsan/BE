package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.ScrapDTO;

import java.util.List;

@Mapper
public interface ScrapMapper {
    List<ScrapDTO> findAll();
    ScrapDTO findById(Long id);
    void insert(ScrapDTO scrapDTO);
    void delete(Long id);
}
