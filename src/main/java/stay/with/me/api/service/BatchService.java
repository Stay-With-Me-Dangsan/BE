package stay.with.me.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stay.with.me.api.model.dto.CommunityDto;
import stay.with.me.api.model.mapper.CommunityMapper;

import java.util.*;

@Slf4j
@Service
public class BatchService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private CommunityMapper communityMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void transferDataToPostgres() {
        try {
            Set<String> keys = redisTemplate.keys("chat:*");
            if (keys == null || keys.isEmpty()) {
                System.out.println("No data in Redis");
                return;
            }

            List<CommunityDto> dataList = new ArrayList<>();

            for (String key : keys) {
                CommunityDto chat = new CommunityDto();
                String jsonValue = redisTemplate.opsForValue().get(key);
                if (jsonValue != null) {
                    Map<String, Object> data = convertJsonToMap(jsonValue);
                    chat.setChatId(data.get("chatId").toString());
                    chat.setUserId(data.get("userId").toString());
                    chat.setDistrict(data.get("district").toString());
                    chat.setMsg(data.get("msg").toString());
                    chat.setMsgDt(data.get("msgDt").toString());
                    dataList.add(chat);
                }
            }
            communityMapper.savePostgresChat(dataList);

        } catch(Exception e) {
            log.debug("redis to postgres 백업 스케줄링 실패");
            e.printStackTrace();
        }
    }

    private Map<String, Object> convertJsonToMap(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("json to map 변환 실패");
            return Collections.emptyMap();
        }
    }
}
