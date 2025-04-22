package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseFileDto;
import stay.with.me.api.model.dto.HouseMainDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface HouseMapper {

    HouseMainDto getMain(int houseMainId);

    HouseDetailDto getDetail(int houseDetailId);

    List<HouseDetailDto> getDetails(float minX, float minY, float maxX, float maxY);

    List<Integer> getDetailsByCondition(Map<String, Object> param);

    int createMain(HouseMainDto param);

    int createDetail(HouseDetailDto param);

    void createFile(HouseFileDto param);

    int updateDetail(HouseDetailDto param);

    boolean deleteDetail(int houseDetailId);
}
