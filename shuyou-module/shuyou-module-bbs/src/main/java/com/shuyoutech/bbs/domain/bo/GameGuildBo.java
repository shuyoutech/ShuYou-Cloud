package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.GameGuildEntity;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-10 11:52:43
 **/
@Data
@AutoMapper(target = GameGuildEntity.class)
@Schema(description = "游戏公会类")
public class GameGuildBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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
