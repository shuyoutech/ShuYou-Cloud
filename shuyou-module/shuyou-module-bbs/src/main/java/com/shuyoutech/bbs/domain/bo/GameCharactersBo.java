package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.GameCharactersEntity;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-10 12:13:36
 **/
@Data
@AutoMapper(target = GameCharactersEntity.class)
@Schema(description = "游戏角色类")
public class GameCharactersBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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
