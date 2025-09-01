package com.shuyoutech.game.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-10 12:13:36
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏角色显示类")
public class GameCharactersVo extends BaseVo {

    @Schema(description = "角色名称")
    private String charactersName;

    @Schema(description = "游戏职业")
    private String gameRole;

    @Schema(description = "游戏等级")
    private Integer gameLevel;

    @Schema(description = "游戏区")
    private String gameArea;

    @Schema(description = "游戏服")
    private String gameServer;

    @Schema(description = "跨区")
    private String crossArea;

    @Schema(description = "游戏种族")
    private String gameRace;

    @Schema(description = "游戏性别")
    private String gameSex;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "军团id")
    private String legionId;

    @Schema(description = "军团名称")
    private String legionName;

}
