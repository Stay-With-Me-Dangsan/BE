package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommonCodeDto;
import stay.with.me.api.model.mapper.CommonCodeMapper;
import stay.with.me.api.service.CommonCodeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;

    @Override
    public List<CommonCodeDto> get(String groupId, String key) throws Exception {
        return commonCodeMapper.get(groupId, key);
    }

    @Override
    public int create(CommonCodeDto param) throws Exception {
        return commonCodeMapper.create(param);
    }

    @Override
    public int update(CommonCodeDto param) throws Exception {
        return commonCodeMapper.update(param);
    }

    @Override
    public boolean delete(int commonCodeId) throws Exception {
        return commonCodeMapper.delete(commonCodeId);
    }
}
