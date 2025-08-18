package com.shuyoutech.common.websocket.handler;

import com.shuyoutech.common.core.constant.CommonConstants;
import com.shuyoutech.common.disruptor.model.DisruptorData;
import com.shuyoutech.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.TimeUnit;

import static com.shuyoutech.common.disruptor.init.DisruptorRunner.disruptorProducer;

/**
 * @author YangChao
 * @date 2025-08-18 20:00
 **/
@Slf4j
@Component
public class PlusWebSocketHandler extends TextWebSocketHandler {

    public static final String WEB_SOCKET_SERVICE = "webSocketServiceImpl";
    public static final String REDIS_WEBSOCKET_PREFIX = "webSocket:";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        log.info("WebSocket connectionEstablished userId:{}, sessionId:{}", userId, sessionId);
        RedisUtils.set(REDIS_WEBSOCKET_PREFIX + userId, session, 1L, TimeUnit.DAYS);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        log.info("handleTextMessage ==================== userId:{},session:{}, payload：{}", userId, session.getId(), payload);
        // session.sendMessage(new TextMessage(replyMessage));
        DisruptorData disruptorData = new DisruptorData();
        disruptorData.setServiceName(WEB_SOCKET_SERVICE);
        disruptorData.setData(payload);
        disruptorProducer.pushData(disruptorData);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        log.info("WebSocket connectionClosed userId:{},sessionId:{},code:{}", userId, sessionId, status.getCode());
        RedisUtils.delete(REDIS_WEBSOCKET_PREFIX + userId);
    }

}
