package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GamePointEventVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-07-10 12:39:57
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GamePointEventVo.class)
@Document(collection = "game_point_event")
@Schema(description = "游戏积分活动表类")
public class GamePointEventEntity extends BaseEntity<GamePointEventEntity> {

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "活动名称")
    private String eventName;

    @Schema(description = "活动日期")
    private Date eventDate;

    @Schema(description = "活动积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal eventPoint;

}
