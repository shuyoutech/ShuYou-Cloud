package com.shuyoutech.game.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.game.domain.entity.GamePointEventEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-10 12:39:57
 **/
@Data
@AutoMapper(target = GamePointEventEntity.class)
@Schema(description = "游戏积分活动类")
public class GamePointEventBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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

    @Schema(description = "开始日期")
    private Date startDate;

    @Schema(description = "结束日期")
    private Date endDate;

}
