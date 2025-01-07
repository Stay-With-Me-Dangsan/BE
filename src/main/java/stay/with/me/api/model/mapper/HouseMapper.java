package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.HouseDto;

import java.util.List;

@Mapper
public interface HouseMapper {

    List<HouseDto> getDetail(HouseDto param);

    List<HouseDto> getDetails(List<HouseDto> params);

    void createDetail(HouseDto param);

    void updateDetail(HouseDto param);

    void deleteDetail(HouseDto param);
}
