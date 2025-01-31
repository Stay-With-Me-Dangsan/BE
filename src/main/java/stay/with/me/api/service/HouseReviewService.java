package stay.with.me.api.service;

import stay.with.me.api.model.dto.HouseReviewDto;

import java.util.List;

public interface HouseReviewService {

    HouseReviewDto get(int reviewId) throws Exception;

    List<HouseReviewDto> getList(int houseDetailId) throws Exception;

    int create(HouseReviewDto param) throws Exception;

    int update(HouseReviewDto param) throws Exception;

    boolean delete(int commonCodeId) throws Exception;

}
