package stay.with.me.api.model.dto;

import lombok.Data;

@Data
public class UploadFilesDTO {
    private Long uploadId;
    private Integer userId;
    private String fileName;
    private String fileUrl;
    private String createdAt;
}
