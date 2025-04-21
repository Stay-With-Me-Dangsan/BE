package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stay.with.me.api.model.dto.ClusterWithHousesDto;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseFileDto;
import stay.with.me.api.model.dto.HouseMainDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface HouseMapper {

    HouseMainDto getMain(int houseMainId);

    HouseDetailDto getDetail(int houseDetailId);

    List<HouseDetailDto> getDetails(double  minX, double  minY, double  maxX, double  maxY);

    List<Integer> getDetailsByCondition(Map<String, Object> param);

    int createMain(HouseMainDto param);

    int createDetail(HouseDetailDto param);

    void createFile(HouseFileDto param);

    int updateDetail(HouseDetailDto param);

    boolean deleteDetail(int houseDetailId);


    List<ClusterWithHousesDto> getMainClusterWithHouses();

    List<ClusterWithHousesDto> getClusterWithHouses(double  minX, double  minY, double  maxX, double  maxY);

    List<HouseDetailDto> getHousesByRoundedLocation(@Param("lat") BigDecimal lat,@Param("lat")BigDecimal lng);

    List<HouseDetailDto> getRecentView(Long userId);

    List<HouseDetailDto> getMarkedHouse(Long userId);

    List<HouseDetailDto> getHousesByUserId(Long userId);
}
