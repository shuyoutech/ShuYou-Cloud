package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.api.enums.GoodsTypeEnum;
import com.shuyoutech.api.enums.SellTypeEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-10 10:20:27
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏商品显示类")
public class GameGoodsVo extends BaseVo {

    @Schema(description = "游戏商城ID")
    private String shopId;

    @Schema(description = "游戏商城名称")
    private String shopName;

    @Schema(description = "游戏ID")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "订单id")
    private String bookId;

    @Schema(description = "商品标题")
    private String goodsTitle;

    /**
     * 枚举 {@link GoodsTypeEnum}
     */
    @Schema(description = "商品类型")
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
    private Integer amount;

    @Schema(description = "单价")
    private Double unitPrice;

    @Schema(description = "价格提示 转成比例1元=34.35万基纳 1万基纳=0.0291元")
    private String priceHint;

    @Schema(description = "商品库存")
    private Integer goodsStock;

    /**
     * 枚举 {@link SellTypeEnum}
     */
    @Schema(description = "出售模式 寄售、担保")
    private String sellType;

    @Schema(description = "商品链接地址")
    private String goodsUrl;

}
