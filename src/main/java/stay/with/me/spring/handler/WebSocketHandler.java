package stay.with.me.spring.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import stay.with.me.api.model.dto.CommunityDto;
import stay.with.me.api.service.RedisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    // 채팅방 별 세션 목록을 관리하는 Map
    private static Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();
    private final RedisService redisService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String userId = "admin";
//        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> headers = session.getHandshakeHeaders();
        List<String> authHeaders = headers.get("Authorization");
        if(authHeaders != null && !authHeaders.isEmpty()) {
            String authToken = authHeaders.get(0);
            System.out.println("Token : "+authToken);
        }

        log.info("Received message: " + payload);

        // 세션에서 채팅방 ID 가져오기
        String roomId = getRoomIdFromSession(session);
        if (roomId == null) return;

        // 채팅방에 속한 모든 세션에 메시지 전송
        for (WebSocketSession sess : chatRooms.getOrDefault(roomId, new ArrayList<>())) {
            sess.sendMessage(message);
        }

        // redis 데이터 저장
        CommunityDto chat = new CommunityDto();
        chat.setUserId(userId);
        chat.setMsg(payload);
        chat.setDistrict(roomId);
        redisService.saveChat(chat, roomId);
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromSession(session);
        if (roomId == null) {
            log.warn("roomId is missing!");
            session.close(CloseStatus.BAD_DATA);
            return;
        }


        chatRooms.putIfAbsent(roomId, new ArrayList<>());
        chatRooms.get(roomId).add(session);

        log.info("Client connected to room {}: {}", roomId, session);
    }

    /* Client가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomIdFromSession(session);
        if (roomId == null) return;

        chatRooms.getOrDefault(roomId, new ArrayList<>()).remove(session);
        log.info("Client disconnected from room {}: {}", roomId, session);
    }

    // WebSocketSession에서 채팅방 ID 추출하는 메서드
    private String getRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();  // 쿼리 파라미터 가져오기
        if (query != null && query.startsWith("roomId=")) {
            return query.substring(7); // "roomId=" 길이만큼 자르기
        }
        return null;
    }
}