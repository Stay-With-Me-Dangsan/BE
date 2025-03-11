package stay.with.me.api.model.mapper;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailCodeMapper {
    void saveCode(String email, String code);

    String getCode(String email);

    void deleteCode(String email);


}
