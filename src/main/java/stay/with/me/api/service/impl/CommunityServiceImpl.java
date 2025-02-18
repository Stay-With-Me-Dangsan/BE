package stay.with.me.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;
import stay.with.me.api.model.mapper.CommunityMapper;
import stay.with.me.api.service.CommunityService;
import stay.with.me.api.service.RedisService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<CommunityDto> getChat(String district) throws Exception {
        String keyPattern = "chat:" + district + ":*";
        Set<String> keys = redisTemplate.keys(keyPattern);

        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        List<CommunityDto> chatList = redisTemplate.opsForValue()
                .multiGet(keys)
                .stream()
                .filter(json -> json != null)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, CommunityDto.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(chat -> chat != null)
                .collect(Collectors.toList());
        String lastConDt = getData("last_saved_time" + district);
        List<CommunityDto> postgresChat = communityMapper.getPostgresChat(district, lastConDt);
        chatList.addAll(postgresChat);
        return chatList;
    }

    @Override
    public void saveChat(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    private String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
