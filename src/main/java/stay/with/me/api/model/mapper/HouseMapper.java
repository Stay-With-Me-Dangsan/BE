package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import stay.with.me.api.model.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface HouseMapper {

    HouseMainDto getMain(int houseMainId);

    HouseDetailDto getDetail(int houseDetailId, Long userId);

    List<HouseDetailDto> getDetails(double  minX, double  minY, double  maxX, double  maxY);

    List<ClusterWithHousesDto> getDetailsByCondition(HouseFilterDto param);

    int createMain(HouseMainDto param);

    int createDetail(HouseDetailDto param);

    void createFile(HouseFileDto param);

    int updateDetail(HouseDetailDto param);

    boolean deleteDetail(int houseDetailId);

    List<ClusterWithHousesDto> getMainClusterWithHouses(int precision);

    List<ClusterWithHousesDto> getClusterWithHouses(double  minX, double  minY, double  maxX, double  maxY, int precision);


    List<HouseDetailDto> getRecentView(Long userId);

    List<HouseDetailDto> getMarkedHouse(Long userId);

    List<HouseDetailDto> getHousesByUserId(Long userId);

    int insertLike(Long userId, int houseDetailId) throws Exception;

    int deleteLike(Long userId, int houseDetailId) throws Exception;
}
