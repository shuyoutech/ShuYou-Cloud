package com.shuyoutech.game.domain.entity;

import com.shuyoutech.api.enums.GameTypeEnum;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GameVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameVo.class)
@Document(collection = "game")
@Schema(description = "游戏表类")
public class GameEntity extends BaseEntity<GameEntity> {

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
