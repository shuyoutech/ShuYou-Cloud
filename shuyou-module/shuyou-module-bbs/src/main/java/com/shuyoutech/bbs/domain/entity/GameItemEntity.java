package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.bbs.domain.vo.GameItemVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-02-12 16:24:49
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameItemVo.class)
@Document(collection = "bbs_game_item")
@Schema(description = "游戏物品表类")
public class GameItemEntity extends BaseEntity<GameItemEntity> {

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品等级")
    private Integer itemLevel;

    @Schema(description = "物品成本价格")
    @Field(targetType = DECIMAL128)
    private BigDecimal itemCostPrice;

    @Schema(description = "物品采购价格")
    @Field(targetType = DECIMAL128)
    private BigDecimal itemBuyPrice;

    @Schema(description = "物品卖出价格")
    @Field(targetType = DECIMAL128)
    private BigDecimal itemSellPrice;

    @Schema(description = "价格小数点")
    private Integer priceScale;

    @Schema(description = "游戏物品材料集合")
    private List<GameItemMaterial> itemMaterialList;

}
