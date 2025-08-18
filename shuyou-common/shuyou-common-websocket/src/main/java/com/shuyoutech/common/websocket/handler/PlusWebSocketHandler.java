package com.shuyoutech.common.websocket.handler;

import com.shuyoutech.common.core.constant.CommonConstants;
import com.shuyoutech.common.disruptor.model.DisruptorData;
import com.shuyoutech.common.websocket.model.WebSocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.shuyoutech.common.disruptor.init.DisruptorRunner.disruptorProducer;

/**
 * @author YangChao
 * @date 2025-08-18 20:00
 **/
@Slf4j
@Component
public class PlusWebSocketHandler extends TextWebSocketHandler {

    public static final String WEB_SOCKET_SERVICE = "webSocketServiceImpl";
    public static final Map<String, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        log.info("WebSocket connectionEstablished userId:{}, sessionId:{}", userId, sessionId);
        USER_SESSION_MAP.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        log.info("handleTextMessage ==================== userId:{},session:{}, payload：{}", userId, session.getId(), payload);
        DisruptorData disruptorData = new DisruptorData();
        disruptorData.setServiceName(WEB_SOCKET_SERVICE);
        disruptorData.setData(WebSocketMsg.builder().userId(userId).message(payload).build());
        disruptorProducer.pushData(disruptorData);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            String sessionId = session.getId();
            String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
            log.info("WebSocket connectionClosed userId:{},sessionId:{},code:{}", userId, sessionId, status.getCode());
            WebSocketSession webSocketSession = USER_SESSION_MAP.remove(userId);
            webSocketSession.close(CloseStatus.BAD_DATA);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
