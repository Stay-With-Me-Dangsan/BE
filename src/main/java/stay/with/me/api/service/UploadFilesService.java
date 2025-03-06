package stay.with.me.api.service;

import stay.with.me.api.model.dto.UploadFilesDTO;

import java.util.List;

public interface UploadFilesService {
    List<UploadFilesDTO> getAllUploads();
    UploadFilesDTO getUploadById(Long id);
    void createUpload(UploadFilesDTO uploadDTO);
    void deleteUpload(Long id);
}
