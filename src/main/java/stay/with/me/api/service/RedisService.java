package stay.with.me.api.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY = "chat";
    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void saveChat(List<CommunityDto> chatList, String district) {
        for(CommunityDto chat : chatList) {
            String chatKey = KEY + ":" + chat.getChatId();
            hashOperations.put(KEY + ":" + chat.getChatId(), "userId", chat.getUserId());
            hashOperations.put(KEY + ":" + chat.getChatId(), "district", district);
            hashOperations.put(KEY + ":" + chat.getChatId(), "msg", chat.getMsg());
            hashOperations.put(KEY + ":" + chat.getChatId(), "msgDt", chat.getMsgDt());

            redisTemplate.expire(chatKey, 30, TimeUnit.MINUTES);
        }
    }
}
