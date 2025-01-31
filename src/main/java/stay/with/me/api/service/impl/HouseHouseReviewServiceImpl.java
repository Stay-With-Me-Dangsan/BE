package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.HouseReviewDto;
import stay.with.me.api.model.mapper.HouseReviewMapper;
import stay.with.me.api.service.HouseReviewService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseHouseReviewServiceImpl implements HouseReviewService {

    private final HouseReviewMapper houseReviewMapper;

    @Override
    public HouseReviewDto get(int reviewId) throws Exception {
        return houseReviewMapper.get(reviewId);
    }

    @Override
    public List<HouseReviewDto> getList(int houseDetailId) throws Exception {
        return houseReviewMapper.getList(houseDetailId);
    }

    @Override
    public int create(HouseReviewDto param) throws Exception {
        return houseReviewMapper.create(param);
    }

    @Override
    public int update(HouseReviewDto param) throws Exception {
        return houseReviewMapper.update(param);
    }

    @Override
    public boolean delete(int commonCodeId) throws Exception {
        return houseReviewMapper.delete(commonCodeId);
    }
}
