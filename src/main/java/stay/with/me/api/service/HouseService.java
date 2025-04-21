package stay.with.me.api.service;

import stay.with.me.api.model.dto.*;

import java.util.List;
import java.util.Map;

public interface HouseService {

    HouseMainDto getMain(int houseMainId) throws Exception;

    HouseDetailDto getDetail(int houseDetailId) throws Exception;

    List<HouseDetailDto> getDetails(double  minX, double  minY, double  maxX, double  maxY) throws Exception;

    List<Integer> getDetailsByCondition(Map<String, Object> param) throws Exception;

    int createMain(HouseMainDto param) throws Exception;

    int createDetail(HouseDetailDto param) throws Exception;

    void createFile(HouseFileDto param) throws Exception;

    int updateDetail(HouseDetailDto param) throws Exception;

    boolean deleteDetail(int houseDetailId) throws Exception;

    List<ClusterWithHousesDto> getMainClusteredHouses() throws Exception;

    List<ClusterWithHousesDto> getClusteredHouses(double  minX, double  minY, double  maxX, double  maxY) throws Exception;

    List<HouseDetailDto> getHousesByUserId(Long userId) throws Exception;

    List<HouseDetailDto> getMarkedHouse(Long userId)throws Exception;

    List<HouseDetailDto> getRecentView(Long userId)throws Exception;

}
