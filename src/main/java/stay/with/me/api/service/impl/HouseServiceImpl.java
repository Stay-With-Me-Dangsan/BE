package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.*;
import stay.with.me.api.model.mapper.HouseMapper;
import stay.with.me.api.service.HouseService;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;

    @Override
    public HouseMainDto getMain(int houseMainId) throws Exception {
        return houseMapper.getMain(houseMainId);
    }

    @Override
    public HouseDetailDto getDetail(int houseDetailId, Long userId) throws Exception {
        return houseMapper.getDetail(houseDetailId, userId);
    }

    @Override
    public List<HouseDetailDto> getDetails(double  minX, double  minY, double  maxX, double  maxY) throws Exception {
        return houseMapper.getDetails(minX, minY, maxX, maxY);
    }

    @Override
    public List<ClusterWithHousesDto> getDetailsByCondition(HouseFilterDto param) throws Exception {
        return houseMapper.getDetailsByCondition(param);
    }

    @Override
    public int createMain(HouseMainDto param) throws Exception {
        return houseMapper.createMain(param);
    }

    @Override
    public int createDetail(HouseDetailDto param) throws Exception {
        return houseMapper.createDetail(param);
    }

    @Override
    public void createFile(HouseFileDto param) throws Exception {
        houseMapper.createFile(param);
    }

    @Override
    public int updateDetail(HouseDetailDto param) throws Exception {
        return houseMapper.updateDetail(param);
    }
    @Override
    public boolean deleteDetail(int houseDetailId) throws Exception {
        return houseMapper.deleteDetail(houseDetailId);
    }
    @Override
    public List<ClusterWithHousesDto> getMainClusteredHouses() throws Exception{
        int precision = 1;
        return houseMapper.getMainClusterWithHouses(precision);
    }

    @Override
    public List<ClusterWithHousesDto> getClusteredHouses( double minX, double minY, double maxX, double maxY, int precision) throws Exception{
            List<ClusterWithHousesDto> result = houseMapper.getClusterWithHouses(minX, minY, maxX, maxY, precision);
            return result;
    }


    @Override
    public List<HouseDetailDto> getHousesByUserId(Long userId) throws Exception {
        return houseMapper.getHousesByUserId(userId);
    }

    @Override
    public List<HouseDetailDto> getMarkedHouse(Long userId) throws Exception {
        return houseMapper.getMarkedHouse(userId);
    }

    @Override
    public int insertLike(Long userId, int houseDetailId) throws Exception {
        return houseMapper.insertLike(userId, houseDetailId);
    }

    @Override
    public int deleteLike(Long userId, int houseDetailId) throws Exception {
        return houseMapper.deleteLike(userId, houseDetailId);
    }

    @Override
    public List<HouseDetailDto> getRecentView(Long userId) throws Exception {
        return houseMapper.getRecentView(userId);
    }




}
