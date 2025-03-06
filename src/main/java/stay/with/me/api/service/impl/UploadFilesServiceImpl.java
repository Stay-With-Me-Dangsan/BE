package stay.with.me.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.UploadFilesDTO;
import stay.with.me.api.model.mapper.UploadFilesMapper;
import stay.with.me.api.service.UploadFilesService;

import java.util.List;

@Service
public class UploadFilesServiceImpl implements UploadFilesService {

    @Autowired
    private UploadFilesMapper uploadFilesMapper;

    @Override
    public List<UploadFilesDTO> getAllUploads() {
        return uploadFilesMapper.findAll();
    }

    @Override
    public UploadFilesDTO getUploadById(Long id) {
        return uploadFilesMapper.findById(id);
    }

    @Override
    public void createUpload(UploadFilesDTO uploadDTO) {
        uploadFilesMapper.insert(uploadDTO);
    }

    @Override
    public void deleteUpload(Long id) {
        uploadFilesMapper.delete(id);
    }
}
