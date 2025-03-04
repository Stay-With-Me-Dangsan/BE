package stay.with.me.api.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stay.with.me.api.model.dto.LoginDTO;
import stay.with.me.api.model.dto.TokenDto;
import stay.with.me.api.model.dto.UserDto;

public interface UserService {

    int signup(UserDto userDto) throws Exception;

    TokenDto signIn(LoginDTO loginDto, HttpServletResponse response);

    int updateNickname(UserDto userDto);

    int updateMypage(UserDto userDto);

    String findEmail(String nickname, String birth);

    boolean sendTemporaryPassword(String email);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId) throws Exception;

    void logoutUser(Long userId, HttpServletResponse response);

}



