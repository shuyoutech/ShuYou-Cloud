package com.shuyoutech.game.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏玩家显示类")
public class GamePlayerVo extends BaseVo {

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "玩家名称")
    private String playerName;

    @Schema(description = "游戏角色ID集合")
    private List<String> charactersIds;

}
