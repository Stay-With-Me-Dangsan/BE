package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseMainDto;

import java.util.List;

@Mapper
public interface HouseMapper {

    HouseMainDto getMain(int houseMainId);

    HouseDetailDto getDetail(int houseDetailId);

    List<HouseDetailDto> getDetails(int minX, int minY, int maxX, int maxY);

    int createMain(HouseMainDto param);

    int createDetail(HouseDetailDto param);

    int updateDetail(HouseDetailDto param);

    boolean deleteDetail(int houseDetailId);
}
