package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-10 12:39:57
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏积分活动显示类")
public class GamePointEventVo extends BaseVo {

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "活动名称")
    private String eventName;

    @Schema(description = "活动日期")
    private Date eventDate;

    @Schema(description = "活动积分")
    private BigDecimal eventPoint;

}
