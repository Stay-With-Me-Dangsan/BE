package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonCodeDto {

    private int commonCodeId;
    private String commonCodeGroupId;
    private String commonCodeKey;
    private String commonCodeName;
    private String commonCodeDescription;

}
