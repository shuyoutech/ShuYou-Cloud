package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.GamePointRecordEntity;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-07-10 12:49:10
 **/
@Data
@AutoMapper(target = GamePointRecordEntity.class)
@Schema(description = "游戏积分记录类")
public class GamePointRecordBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "玩家id")
    private String playerId;

    @Schema(description = "玩家名称")
    private String playerName;

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "军团id")
    private String legionId;

    @Schema(description = "军团名称")
    private String legionName;

    @Schema(description = "游戏角色id")
    private String charactersId;

    @Schema(description = "游戏角色名称")
    private String charactersName;

    @Schema(description = "积分活动id")
    private String eventId;

    @Schema(description = "积分活动名称")
    private String eventName;

    @Schema(description = "积分日期")
    private Date pointDate;

    @Schema(description = "积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal point;

    @Schema(description = "开始日期")
    private Date startDate;

    @Schema(description = "开始日期")
    private Date endDate;

}
