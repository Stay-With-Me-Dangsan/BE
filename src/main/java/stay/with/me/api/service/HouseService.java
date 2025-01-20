package stay.with.me.api.service;

import stay.with.me.api.model.dto.HouseDetailDto;
import stay.with.me.api.model.dto.HouseMainDto;

import java.util.List;

public interface HouseService {

    HouseMainDto getMain(int houseMainId) throws Exception;

    HouseDetailDto getDetail(int houseDetailId) throws Exception;

    List<HouseDetailDto> getDetails(int minX, int minY, int maxX, int maxY) throws Exception;

    int createMain(HouseMainDto param) throws Exception;

    int createDetail(HouseDetailDto param) throws Exception;

    int updateDetail(HouseDetailDto param) throws Exception;

    boolean deleteDetail(int houseDetailId) throws Exception;
}
