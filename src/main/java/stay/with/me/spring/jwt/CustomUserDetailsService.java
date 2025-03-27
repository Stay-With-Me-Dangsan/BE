package stay.with.me.spring.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.user.UserDto;
import stay.with.me.api.model.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDto userDto = userMapper.findByEmail(email);
        if (userDto == null) { //없는 회원일 경우 예외처리
            throw new UsernameNotFoundException("Email" + email + "을 찾을수 없습니다");
        }

        return new stay.with.me.spring.jwt.CustomUserDetails(userDto);
    }


    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        UserDto userDto = userMapper.findById(userId);
        if (userDto == null) {
            throw new UsernameNotFoundException("User ID " + userId + "을 찾을 수 없습니다");
        }
        return new CustomUserDetails(userDto);
    }

}
