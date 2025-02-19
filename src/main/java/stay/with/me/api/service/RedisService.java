package stay.with.me.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public void saveChat(List<CommunityDto> chatList, String district) {
        for(CommunityDto chat : chatList) {
            String chatKey = KEY + ":" + district + ":" + chat.getChatId();
            try {
                String json = objectMapper.writeValueAsString(chat);
                logger.info(">>>>> json: {}", json);
                redisTemplate.opsForValue().set(chatKey, json, 30, TimeUnit.MINUTES);
            } catch(JsonProcessingException e) {
                logger.error(">>>>> json 변환 실패: {}", chat, e);
            }
            redisTemplate.expire(chatKey, 30, TimeUnit.MINUTES);
        }
    }
}
