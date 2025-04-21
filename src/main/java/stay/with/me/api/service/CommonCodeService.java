package stay.with.me.api.service;

import stay.with.me.api.model.dto.CommonCodeDto;

import java.util.List;

public interface CommonCodeService {

    List<CommonCodeDto> getList() throws Exception;

    List<CommonCodeDto> get(String groupId, String key) throws Exception;

    int create(CommonCodeDto param) throws Exception;

    int update(CommonCodeDto param) throws Exception;

    boolean delete(int commonCodeId) throws Exception;


}
