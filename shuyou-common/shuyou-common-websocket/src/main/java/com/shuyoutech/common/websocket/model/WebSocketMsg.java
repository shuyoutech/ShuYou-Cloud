package com.shuyoutech.common.websocket.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:07
 **/
@Data
@Builder
@Schema(description = "消息类")
public class WebSocketMsg implements Serializable {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "消息内容")
    private String message;

}
