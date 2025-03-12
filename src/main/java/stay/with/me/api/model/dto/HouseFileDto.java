package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseFileDto {

    private int houseDetailId;
    private String houseFileName;
    private String houseFilePath;
    private int houseFileWidth;
    private int houseFileHeight;
    private String houseFileExtension;
    private String createdBy;

}
