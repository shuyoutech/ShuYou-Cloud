package com.shuyoutech.bbs.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
@Data
@Schema(description = "游戏玩家积分清算类")
public class GamePlayerPointSettleBo implements Serializable {

    @Schema(description = "玩家IDS")
    private Set<String> playerIds;

    @Schema(description = "积分")
    private BigDecimal point;

}
