package stay.with.me.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stay.with.me.api.model.dto.UploadFilesDTO;
import stay.with.me.api.service.UploadFilesService;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
public class UploadFilesController {

    @Autowired
    private UploadFilesService uploadFilesService;

    // 모든 업로드 파일 조회
    @GetMapping
    public List<UploadFilesDTO> getAllUploads() {
        // 모든 업로드된 파일 반환
        return uploadFilesService.getAllUploads();
    }

    // 특정 업로드 파일 조회 (파일 ID로 조회)
    @GetMapping("/{id}")
    public UploadFilesDTO getUploadById(@PathVariable Long id) {
        // 특정 파일 ID에 해당하는 업로드 정보 반환
        return uploadFilesService.getUploadById(id);
    }

    // 파일 업로드
    @PostMapping
    public void createUpload(@RequestBody UploadFilesDTO uploadDTO) {
        // 클라이언트 요청 데이터를 기반으로 파일 업로드 생성
        uploadFilesService.createUpload(uploadDTO);
    }

    // 파일 삭제
    @DeleteMapping("/{id}")
    public void deleteUpload(@PathVariable Long id) {
        // 특정 파일 ID에 해당하는 업로드 정보 삭제
        uploadFilesService.deleteUpload(id);
    }
}
