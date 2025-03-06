package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.ReportDTO;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportDTO> findAll();
    ReportDTO findById(Long id);
    void insert(ReportDTO reportDTO);
    void update(Long id, ReportDTO reportDTO);
    void delete(Long id);
}
