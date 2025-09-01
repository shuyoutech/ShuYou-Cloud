package com.shuyoutech.game.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 */
@Data
@Builder
public class GameAreaServer implements Serializable {

    @Schema(description = "游戏区")
    private String gameArea;

    @Schema(description = "游戏服")
    private String gameServer;

    @Schema(description = "跨区")
    private String crossArea;

}
