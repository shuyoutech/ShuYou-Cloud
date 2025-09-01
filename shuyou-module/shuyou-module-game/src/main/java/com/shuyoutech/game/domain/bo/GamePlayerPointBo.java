package com.shuyoutech.game.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.game.domain.entity.GamePlayerPointEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
@Data
@AutoMapper(target = GamePlayerPointEntity.class)
@Schema(description = "游戏玩家积分类")
public class GamePlayerPointBo implements Serializable {

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

    @Schema(description = "已使用积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal usePoint;

    @Schema(description = "可用积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal availablePoint;

    @Schema(description = "总积分")
    @Field(targetType = DECIMAL128)
    private BigDecimal totalPoint;

    @Schema(description = "玩家ids")
    private Set<String> playerIds;

}
