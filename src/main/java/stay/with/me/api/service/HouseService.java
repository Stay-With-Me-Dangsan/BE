package stay.with.me.api.service;

import stay.with.me.api.model.dto.HouseDto;

import java.util.List;

public interface HouseService {

    List<HouseDto> getDetail(HouseDto param);

    List<HouseDto> getDetails(List<HouseDto> params);

    void createDetail(HouseDto param);

    void updateDetail(HouseDto param);

    void deleteDetail(HouseDto param);

}
