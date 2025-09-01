package com.shuyoutech.crawler.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-07 14:09
 **/
@Data
public class CrawlerGameGoodsBo implements Serializable {

    @NotBlank(message = "游戏ID不能为空")
    @Schema(description = "游戏ID")
    private String gameId;

    @Schema(description = "游戏区")
    private String gameArea;

    @Schema(description = "游戏服")
    private String gameServer;

    @Schema(description = "游戏种族")
    private String gameRace;

}
