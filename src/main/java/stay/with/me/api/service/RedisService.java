package stay.with.me.api.service;

import lombok.RequiredArgsConstructor;
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

    public void saveChat(List<CommunityDto> chatList, String district) {
        for(CommunityDto chat : chatList) {
            String chatKey = KEY + ":" + district + ":" + chat.getChatId();
            redisTemplate.opsForValue().set(chatKey, chat, 30, TimeUnit.MINUTES);
            redisTemplate.expire(chatKey, 30, TimeUnit.MINUTES);
        }
    }
}
