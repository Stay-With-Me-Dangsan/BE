package stay.with.me.api.service;


import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.user.UserInfoDto;

import java.util.List;

public interface AdminService {

    List<UserInfoDto> getAdminUserList() throws Exception;

    List<BoardDTO> getAdminBoardList(String category) throws Exception;

    int updateBoardBlind(List<Long> selectedIds) throws Exception;
}
