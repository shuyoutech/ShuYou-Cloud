package com.shuyoutech.game.domain.bo;

import com.shuyoutech.api.enums.GameTypeEnum;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.game.domain.entity.GameAreaServer;
import com.shuyoutech.game.domain.entity.GameEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 09:46:51
 **/
@Data
@AutoMapper(target = GameEntity.class)
@Schema(description = "游戏类")
public class GameBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "游戏名")
    private String gameName;

    /**
     * 枚举 {@link GameTypeEnum}
     */
    @Schema(description = "游戏类型:网络游戏、手机游戏、网页游戏")
    private String gameType;

    @Schema(description = "游戏名字首字母")
    private String gameInitial;

    @Schema(description = "游戏图标")
    private String gameIcon;

    @Schema(description = "商品类型")
    private List<String> goodsTypes;

    @Schema(description = "跨区")
    private List<String> crossAreas;

    @Schema(description = "游戏区服")
    private List<GameAreaServer> areaServers;

    @Schema(description = "游戏种族阵营")
    private List<String> gameRaces;

    @Schema(description = "游戏角色职业")
    private List<String> gameRoles;

    @Schema(description = "关联商城平台id")
    private List<String> platformIds;

    @Schema(description = "是否热门游戏")
    private Boolean hotFlag;

}
