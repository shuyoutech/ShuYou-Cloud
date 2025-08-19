package com.shuyoutech.api.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author YangChao
 * @date 2025-05-15 13:55
 **/
public interface RemoteAigcService {

    void chat(String userId, String message, WebSocketSession webSocketSession);

}
