package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.ClusterWithHousesDto;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseFileDto;
import stay.with.me.api.model.dto.HouseMainDto;
import stay.with.me.api.model.mapper.HouseMapper;
import stay.with.me.api.service.HouseService;

import java.util.List;
import java.util.Map;


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
    public HouseDetailDto getDetail(int houseDetailId) throws Exception {
        return houseMapper.getDetail(houseDetailId);
    }

    @Override
    public List<HouseDetailDto> getDetails(double  minX, double  minY, double  maxX, double  maxY) throws Exception {
        return houseMapper.getDetails(minX, minY, maxX, maxY);
    }

    @Override
    public List<Integer> getDetailsByCondition(Map<String, Object> param) throws Exception {
        return houseMapper.getDetailsByCondition(param);
    }

    @Override
    public int createMain(HouseMainDto param) throws Exception {
        //TODO houseMainId 코드조합해시
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
        return houseMapper.getMainClusterWithHouses();
    }

    @Override
    public List<ClusterWithHousesDto> getClusteredHouses(double  minX, double  minY, double  maxX, double  maxY) throws Exception{
        try {
            log.info("🔍 getClusteredHouses() 호출됨 - 클러스터 하우스 조회 시작");

            List<ClusterWithHousesDto> result = houseMapper.getClusterWithHouses(minX, minY, maxX, maxY);

            log.info("✅ 클러스터 조회 성공: {}건", result.size());

            return result;
        } catch (Exception e) {
            log.error("❌ 클러스터 하우스 조회 중 예외 발생", e);
            throw e; // 혹은 커스텀 예외로 감싸도 됨
        }
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
    public List<HouseDetailDto> getRecentView(Long userId) throws Exception {
        return houseMapper.getRecentView(userId);
    }

}
