package stay.with.me.spring.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.UserDto;
import stay.with.me.api.model.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDto userDto = userMapper.findByEmail(email); //DB에서 유저가 입력한 ID와 일치하는 정보를 불러온다
        if (userDto == null) { //없는 회원일 경우 예외처리
            throw new UsernameNotFoundException("Email" + email + "을 찾을수 없습니다");
        }
        System.out.println("CustomUserDetails여기");
        return new stay.with.me.spring.jwt.CustomUserDetails(userDto);
    }
}
