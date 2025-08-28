package com.shuyoutech.bbs.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
@Data
@Schema(description = "游戏玩家积分登记类")
public class GamePlayerPointRegisterBo implements Serializable {

    @Schema(description = "活动名称")
    private String eventName;

    @Schema(description = "活动日期")
    private Date eventDate;

    @Schema(description = "活动积分")
    private BigDecimal eventPoint;

    @Schema(description = "活动文件IDS")
    private Set<String> eventFileIds;

    @Schema(description = "游戏角色IDS")
    private Set<String> charactersIds;

}
