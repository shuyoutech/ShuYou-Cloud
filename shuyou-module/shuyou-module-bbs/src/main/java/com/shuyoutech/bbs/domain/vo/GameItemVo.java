package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import com.shuyoutech.bbs.domain.entity.GameItemMaterial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-02-12 16:24:49
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏物品显示类")
public class GameItemVo extends BaseVo {

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品等级")
    private Integer itemLevel;

    @Schema(description = "物品成本价格")
    private BigDecimal itemCostPrice;

    @Schema(description = "物品采购价格")
    private BigDecimal itemBuyPrice;

    @Schema(description = "物品卖出价格")
    private BigDecimal itemSellPrice;

    @Schema(description = "价格小数点")
    private Integer priceScale;

    @Schema(description = "物品成本-卖出利润")
    private BigDecimal itemCostProfit;

    @Schema(description = "物品采购-卖出利润")
    private BigDecimal itemBuyProfit;

    @Schema(description = "游戏物品材料集合")
    private List<GameItemMaterial> itemMaterialList;

}
