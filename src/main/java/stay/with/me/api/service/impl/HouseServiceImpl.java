package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseMainDto;
import stay.with.me.api.model.mapper.HouseMapper;
import stay.with.me.api.service.HouseService;

import java.util.List;

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
    public List<HouseDetailDto> getDetails(int minX, int minY, int maxX, int maxY) throws Exception {
        return houseMapper.getDetails(minX, minY, maxX, maxY);
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
    public int updateDetail(HouseDetailDto param) throws Exception {
        return houseMapper.updateDetail(param);
    }

    @Override
    public boolean deleteDetail(int houseDetailId) throws Exception {
        return houseMapper.deleteDetail(houseDetailId);
    }
}
