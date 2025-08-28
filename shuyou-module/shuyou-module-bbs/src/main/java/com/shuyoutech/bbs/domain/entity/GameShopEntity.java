package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.bbs.domain.vo.GameShopVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameShopVo.class)
@Document(collection = "bbs_game_shop")
@Schema(description = "游戏商城表类")
public class GameShopEntity extends BaseEntity<GameShopEntity> {

    @Schema(description = "商城名称")
    private String shopName;

    @Schema(description = "商城简称")
    private String shopAlias;

    @Schema(description = "商城图标")
    private String shopIcon;

    @Schema(description = "商城地址")
    private String shopUrl;

}
