package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-07 00:32
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "game_goods")
@Schema(description = "游戏商品表类")
public class GameGoodsEntity extends BaseEntity<GameGoodsEntity> {

    @Schema(description = "商品状态 0-待确认更新状态,1-交易发布")
    private String status;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "平台id")
    private String platformId;

    @Schema(description = "平台名称")
    private String platformName;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "订单id")
    private String bookId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "商品类型 账号、游戏币、装备、点券")
    private String goodsType;

    @Schema(description = "跨区")
    private String crossArea;

    @Schema(description = "游戏区")
    private String gameArea;

    @Schema(description = "游戏服")
    private String gameServer;

    @Schema(description = "游戏种族阵营")
    private String gameRace;

    @Schema(description = "游戏角色职业")
    private String gameRole;

    @Schema(description = "游戏角色性别")
    private String gameSex;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "数量")
    private Double amount;

    @Schema(description = "单价")
    private Double unitPrice;

    @Schema(description = "价格提示 转成比例1元=34.35万基纳 1万基纳=0.0291元")
    private String priceHint;

    @Schema(description = "商品库存")
    private Integer goodsStock;

    @Schema(description = "出售模式 寄售、担保")
    private String sellType;

    @Schema(description = "商品链接地址")
    private String goodsUrl;

}
