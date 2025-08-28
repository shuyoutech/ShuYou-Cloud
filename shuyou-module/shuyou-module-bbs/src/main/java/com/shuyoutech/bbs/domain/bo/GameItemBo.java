package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.GameItemEntity;
import com.shuyoutech.bbs.domain.entity.GameItemMaterial;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:34:21
 **/
@Data
@AutoMapper(target = GameItemEntity.class)
@Schema(description = "游戏物品类")
public class GameItemBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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

    @Schema(description = "游戏物品材料集合")
    private List<GameItemMaterial> itemMaterialList;

}
