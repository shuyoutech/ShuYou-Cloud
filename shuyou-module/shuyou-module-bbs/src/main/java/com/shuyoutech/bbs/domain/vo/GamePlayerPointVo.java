package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏玩家积分显示类")
public class GamePlayerPointVo extends BaseVo {

    @Schema(description = "玩家id")
    private String playerId;

    @Schema(description = "玩家名称")
    private String playerName;

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "已使用积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal usePoint;

    @Schema(description = "可用积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal availablePoint;

    @Schema(description = "总积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal totalPoint;

}
