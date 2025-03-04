package stay.with.me.spring.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        System.out.println("Expired Key: " + expiredKey);

        processExpiredKey(expiredKey);
    }

    private void processExpiredKey(String key) {
        System.out.println("Processing expired key: " + key);
    }
}

