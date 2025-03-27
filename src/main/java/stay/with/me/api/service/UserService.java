package stay.with.me.api.service;


import jakarta.servlet.http.HttpServletResponse;
import stay.with.me.api.model.dto.user.LoginDTO;
import stay.with.me.api.model.dto.user.TokenDto;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.dto.user.UserInfoDto;

public interface UserService {

    int signup(UserDto userDto) throws Exception;

    TokenDto signIn(LoginDTO loginDto, HttpServletResponse response);

    UserDto getUserById(Long userId);

    int updateOuathReg(UserDto userDto);

    int updateNickname(UserDto userDto);

    int updateEmail(UserDto userDto) throws Exception;

    int updatePw(UserDto userDto);

    TokenDto refreshAccessToken(String refreshToken, HttpServletResponse response);


    UserInfoDto findEmail(UserInfoDto userInfoDto);

    boolean sendTemporaryPassword(String email);

    void deleteUser(Long userId) throws Exception;

    void logoutUser(Long userId, HttpServletResponse response);


}



