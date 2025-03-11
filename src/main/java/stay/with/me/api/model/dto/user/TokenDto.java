package stay.with.me.api.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
//    private String refreshToken;
    private Long userId;


}