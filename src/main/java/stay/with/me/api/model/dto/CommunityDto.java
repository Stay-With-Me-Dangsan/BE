package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDto {

    private String userId;
    private String district;
    private String msg;
    private String msgDt;

}
