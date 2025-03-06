package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.UploadFilesDTO;

import java.util.List;

@Mapper
public interface UploadFilesMapper {
    List<UploadFilesDTO> findAll();
    UploadFilesDTO findById(Long id);
    void insert(UploadFilesDTO uploadDTO);
    void delete(Long id);
}
