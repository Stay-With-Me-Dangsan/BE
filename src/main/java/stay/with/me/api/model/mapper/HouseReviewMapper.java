package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.HouseReviewDto;

import java.util.List;

@Mapper
public interface HouseReviewMapper {

    HouseReviewDto get(int reviewId);

    List<HouseReviewDto> getList(int houseDetailId);

    int create(HouseReviewDto param);

    int update(HouseReviewDto param);

    boolean delete(int ReviewDto);
}
