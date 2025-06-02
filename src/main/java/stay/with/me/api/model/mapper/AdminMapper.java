package stay.with.me.api.model.mapper;


import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.BoardDTO;
import stay.with.me.api.model.dto.CommonCodeDto;
import stay.with.me.api.model.dto.user.UserInfoDto;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<UserInfoDto> getAdminUserList();

    List<BoardDTO> getAdminBoardList(String category);

    int updateBoardBlind(List<Long> selectedIds);
}
