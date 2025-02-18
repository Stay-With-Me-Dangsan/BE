package stay.with.me.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;
import stay.with.me.api.model.mapper.CommunityMapper;
import stay.with.me.api.service.CommunityService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        if (keys.size() > 1000) {
            keys = scanKeys(keyPattern);
        }

        List<CommunityDto> chatList = redisTemplate.opsForValue()
                .multiGet(keys)
                .stream()
                .filter(json -> json != null)
                .map(json -> {
                    try {
                        System.out.println("1 >>>>> " + json);
                        return objectMapper.readValue(json, CommunityDto.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(chat -> chat != null)
                .collect(Collectors.toList());
        for(CommunityDto x : chatList) {
            System.out.println("2 >>>>> " + x.toString());
        }
        String lastConDt = getData("last_saved_time:" + district);
        List<CommunityDto> postgresChat = communityMapper.getPostgresChat(district, lastConDt);
        chatList.addAll(postgresChat);
        return chatList;
    }

    private String getData(String key) {
        // TODO redisService로 이동
        return (String) redisTemplate.opsForValue().get(key);
    }

    private Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        return keys;
    }
}
