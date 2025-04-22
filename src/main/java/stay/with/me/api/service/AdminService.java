package stay.with.me.api.service;


import stay.with.me.api.model.dto.BoardListDto;
import stay.with.me.api.model.dto.user.UserInfoDto;

import java.util.List;

public interface AdminService {

    List<UserInfoDto> getUserList() throws Exception;

    List<BoardListDto> getBoardList() throws Exception;
}
