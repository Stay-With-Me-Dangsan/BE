package stay.with.me.spring.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import stay.with.me.api.model.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;



public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private String birth;
    private String role;


    public CustomUserDetails(UserDto userDto) {

        this.userId = userDto.getUserId();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.gender = userDto.getGender();
        this.birth = userDto.getBirth();
        this.role = (userDto.getRole() != null) ? userDto.getRole() : "ROLE_USER"; // 기본 역할 설정
    }


    //가입된 회원의 권한를 불러와 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities  = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role)); // 역할을 GrantedAuthority로 변환하여 추가
        return authorities ;

    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ✅ userId getter 추가
    public Long getUserId() {
        return userId;
    }

    //패스워드 비교를 위해 패스워드 리턴
    @Override
    public String getPassword() {
        return password;
    }
    //email 비교를 위해 리턴
    @Override
    public String getUsername() {
        return email;
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getGender () {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getBirth () {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
    /*
     * 아래의 정보들은 DB에 테이블을 따로 두어 관리할 수 있지만
     * 현재 어플리케이션에서는 사용하지 않을 기능들이기 때문에 전부 true를 리턴한다
     * */
    @Override
    public boolean isAccountNonExpired() { // 계정이 만료되었는지 여부를 리턴한다
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠겼는지 여부를 리턴한다
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 계정 보안정보가 만료되었는지를 리턴한다
        return true;
    }

    @Override
    public boolean isEnabled() { //계정의 비활성화 여부를 리턴한다
        return true;
    }

}
