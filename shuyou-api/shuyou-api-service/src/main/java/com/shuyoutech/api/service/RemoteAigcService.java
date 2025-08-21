package com.shuyoutech.api.service;

import com.shuyoutech.api.model.RemoteModel;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author YangChao
 * @date 2025-05-15 13:55
 **/
public interface RemoteAigcService {

    RemoteModel getModel(String provider, String modelName);

    void chat(String userId, String message, WebSocketSession webSocketSession);

}
