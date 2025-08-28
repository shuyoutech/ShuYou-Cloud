package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.bbs.domain.vo.GameCharactersVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-10 12:13:36
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameCharactersVo.class)
@Document(collection = "bbs_game_characters")
@Schema(description = "游戏角色表类")
public class GameCharactersEntity extends BaseEntity<GameCharactersEntity> {

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
