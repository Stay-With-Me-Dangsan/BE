package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardListDto;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.model.mapper.AdminMapper;
import stay.with.me.api.service.AdminService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public List<UserInfoDto> getUserList() throws Exception {
        return adminMapper.getUserList();
    }

    @Override
    public List<BoardListDto> getBoardList() throws Exception {
        return null;
    }
}
