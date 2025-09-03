package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GameLegionVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-10 12:02:06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameLegionVo.class)
@Document(collection = "game_legion")
@Schema(description = "游戏军团表类")
public class GameLegionEntity extends BaseEntity<GameLegionEntity> {

    @Schema(description = "军团名称")
    private String legionName;

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "军团团长")
    private String legionLeader;

}
