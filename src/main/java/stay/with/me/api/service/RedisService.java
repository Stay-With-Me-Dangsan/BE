package stay.with.me.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY = "chat";

    public void saveChat(CommunityDto chat, String district) {
        Long chatId = redisTemplate.opsForValue().increment("chat_id_seq");
        chat.setChatId(chatId.toString());
        chat.setMsgDt(Instant.now().toString());
        String chatKey = KEY + ":" + district + ":" + chat.getChatId();
        redisTemplate.opsForValue().set(chatKey, chat, 5, TimeUnit.MINUTES);
        redisTemplate.expire(chatKey, 5, TimeUnit.MINUTES);
    }
}
