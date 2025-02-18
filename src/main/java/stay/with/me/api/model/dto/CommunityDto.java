package stay.with.me.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDto implements Serializable {

    private String chatId;
    private String userId;
    private String district;
    private String msg;
    private String msgDt;

}
