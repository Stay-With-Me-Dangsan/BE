package stay.with.me.api.model.dto.user;

import lombok.*;

import java.time.LocalDate;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    //회원 번호 (pk)
    private Long userId;

    private String nickname;
    private String gender;
    private String birth;
    private String email;
    private String password;
    private String provider;
    private String providerId;
    private String role;
    private String accessToken;
    private String refreshToken;
    private LocalDate rtExpiredDate;
    private LocalDate createdDate;
    private LocalDate updatedDate;


    //소셜로 가입시 유저 등록
    public UserDto(Long userId, String email, String provider, String providerId, String gender, String nickname, String birth) {
        this.userId = userId;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.gender = gender;
        this.nickname = nickname;
        this.birth = birth;
    }


}
