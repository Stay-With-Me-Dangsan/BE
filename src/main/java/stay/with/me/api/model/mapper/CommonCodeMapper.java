package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.CommonCodeDto;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

    List<CommonCodeDto> get(String groupId, String key);

    int create(CommonCodeDto param);

    int update(CommonCodeDto param);

    boolean delete(int commonCodeId);
}
