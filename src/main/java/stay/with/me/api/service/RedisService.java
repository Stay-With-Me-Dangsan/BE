package stay.with.me.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY = "chat";

    public void saveChat(CommunityDto chat, String district) {
        String redisKey = "chat_id_seq_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long postgresKey = redisTemplate.opsForValue().increment(redisKey);
        String formattedSeq = String.format("%06d", postgresKey);
        Long chatId = Long.parseLong(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + formattedSeq);
        chat.setChatId(chatId.toString());
        chat.setMsgDt(Instant.now().toString());
        String chatKey = KEY + ":" + district + ":" + chat.getChatId();
        redisTemplate.opsForValue().set(chatKey, chat, 30, TimeUnit.MINUTES);
        redisTemplate.expire(chatKey, 30, TimeUnit.MINUTES);
    }
}
