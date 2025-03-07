package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stay.with.me.api.model.dto.UserDto;


@Mapper
public interface UserMapper {

    int signUp(UserDto userDto);

    UserDto findByEmail(@Param("email") String email);

    void mypagEdit(UserDto userDto);


    int SaveOrUpdateRefreshToken(Long userId, String refreshToken);

    //소셜로그인 정보 가져오기

    UserDto findById(Long userId);

    void insertSocialUser(UserDto newUser);

    void OauthSingUp(UserDto user);

    UserDto findOauthByEmail(String email,String providerId);

    //기존계정에 소셜로그인 계정 업데이트
    void updateSocialLogin(Long userId, String provider, String providerId);

    UserDto findEmail(UserDto userDto);

    //임시비밀번호로 없데이트
    void updateTempPassword(String email, String password);
    //로그아웃 - 리프레시 토큰 삭제
    void deleteRefreshToken(Long userId);

    void deleteUser(Long userId);

    int updateNickname(UserDto userDto);

    int updateMypage(UserDto userDto);


}
