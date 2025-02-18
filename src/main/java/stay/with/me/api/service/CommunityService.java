package stay.with.me.api.service;

import stay.with.me.api.model.dto.CommunityDto;

import java.util.List;

public interface CommunityService {

    List<CommunityDto> getChat(String district) throws Exception;

}
