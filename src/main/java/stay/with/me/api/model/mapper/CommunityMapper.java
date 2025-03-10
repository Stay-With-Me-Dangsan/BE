package stay.with.me.api.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import stay.with.me.api.model.dto.CommunityDto;

import java.util.List;

@Mapper
public interface CommunityMapper {

    List<CommunityDto> getPostgresChat(String district, String lastConDt);

    void savePostgresChat(List<CommunityDto> list);
}
