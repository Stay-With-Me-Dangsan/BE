package stay.with.me.api.model.mapper;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailCodeMapper {// 이메일 찾기

    void saveCode(String email, String code);

    String getCode(String email);

    void deleteCode(String email);


}
