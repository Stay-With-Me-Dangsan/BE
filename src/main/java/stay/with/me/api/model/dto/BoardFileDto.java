package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardFileDto {

    private int boardPostId;
    private String boardFileName;
    private String boardFilePath;
    private int boardFileWidth;
    private int boardFileHeight;
    private String boardFileExtension;
    private String createdBy;
    private String fileType;
}
