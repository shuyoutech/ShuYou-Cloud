package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-10 11:52:43
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏公会显示类")
public class GameGuildVo extends BaseVo {

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
