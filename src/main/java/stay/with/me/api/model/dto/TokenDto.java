package stay.with.me.api.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private UserDto user;




    public TokenDto(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;

    }

    public TokenDto(String newAccessToken, String newRefreshToken) {
    }
}