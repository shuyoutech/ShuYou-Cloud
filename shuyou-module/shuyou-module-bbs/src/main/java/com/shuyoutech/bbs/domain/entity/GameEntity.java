package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.api.enums.GameTypeEnum;
import com.shuyoutech.bbs.domain.vo.GameVo;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameVo.class)
@Document(collection = "bbs_game")
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
    private List<String> goodsTypeList;

    @Schema(description = "跨区")
    private List<String> crossAreaList;

    @Schema(description = "游戏区服")
    private List<GameAreaServer> areaServerList;

    @Schema(description = "游戏种族阵营")
    private List<String> gameRaceList;

    @Schema(description = "游戏角色职业")
    private List<String> gameRoleList;

    @Schema(description = "关联商城平台id")
    private List<String> shopIdList;

    @Schema(description = "热门游戏")
    private Boolean beenHotGame;

}
