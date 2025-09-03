package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GamePlayerVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GamePlayerVo.class)
@Document(collection = "game_player")
@Schema(description = "游戏玩家表类")
public class GamePlayerEntity extends BaseEntity<GamePlayerEntity> {

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "玩家名称")
    private String playerName;

    @Schema(description = "游戏角色ID集合")
    private List<String> charactersIds;

}
