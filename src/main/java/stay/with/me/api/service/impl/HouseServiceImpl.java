package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.HouseDto;
import stay.with.me.api.model.mapper.HouseMapper;
import stay.with.me.api.service.HouseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;

    @Override
    public List<HouseDto> getDetail(HouseDto param) {
        return houseMapper.getDetail(param);
    }

    @Override
    public List<HouseDto> getDetails(List<HouseDto> params) {
        return houseMapper.getDetails(params);
    }

    @Override
    public void createDetail(HouseDto param) {
        houseMapper.createDetail(param);
    }

    @Override
    public void updateDetail(HouseDto param) {
        houseMapper.updateDetail(param);
    }

    @Override
    public void deleteDetail(HouseDto param) {
        houseMapper.deleteDetail(param);
    }

}
