package stay.with.me.api.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String email;
    private String password;
    private String nickname;
    private String birth;
    private LocalDate createdDate;

}
