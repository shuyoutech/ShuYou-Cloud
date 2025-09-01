package com.shuyoutech.game.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.game.domain.entity.GamePlayerEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
@Data
@AutoMapper(target = GamePlayerEntity.class)
@Schema(description = "游戏玩家类")
public class GamePlayerBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "公会id")
    private String guildId;

    @Schema(description = "公会名称")
    private String guildName;

    @Schema(description = "玩家名称")
    private String playerName;

    @Schema(description = "游戏角色ID集合")
    private List<String> charactersIds;

    @Schema(description = "月份")
    private String month;

}
