package stay.with.me.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.user.UserInfoDto;
import stay.with.me.api.model.mapper.AdminMapper;
import stay.with.me.api.service.AdminService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public List<UserInfoDto> getAdminUserList() throws Exception {
        return adminMapper.getAdminUserList();
    }

    @Override
    public List<BoardDTO> getAdminBoardList(String category) throws Exception {
        return adminMapper.getAdminBoardList(category);
    }

    @Override
    public int updateBoardBlind(List<Long> selectedIds) throws Exception {
        return adminMapper.updateBoardBlind(selectedIds);
    }
}
