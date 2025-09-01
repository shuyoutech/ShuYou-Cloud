package com.shuyoutech.game.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-10 12:02:06
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏军团显示类")
public class GameLegionVo extends BaseVo {

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
