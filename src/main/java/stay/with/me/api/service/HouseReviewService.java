package stay.with.me.api.service;

import stay.with.me.api.model.dto.HouseReviewDto;

import java.util.List;
import java.util.Set;

public interface HouseReviewService {

    HouseReviewDto get(int reviewId) throws Exception;

    List<HouseReviewDto> getList(Set<Integer> houseDetailIds) throws Exception;

    int create(HouseReviewDto param) throws Exception;

    int update(HouseReviewDto param) throws Exception;

    boolean delete(int commonCodeId) throws Exception;

}
