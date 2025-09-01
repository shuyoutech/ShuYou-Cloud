package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GameGuildVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameGuildVo.class)
@Document(collection = "game_guild")
@Schema(description = "游戏公会表类")
public class GameGuildEntity extends BaseEntity<GameGuildEntity> {

    /**
     * 枚举 {@link StatusEnum}
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "公会会长")
    private String president;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "yy频道")
    private String yyChannel;

    @Schema(description = "QQ群")
    private String qqGroup;

    @Schema(description = "公会图标")
    private String guildIcon;

    @Schema(description = "公会描述")
    private String guildDesc;

}
