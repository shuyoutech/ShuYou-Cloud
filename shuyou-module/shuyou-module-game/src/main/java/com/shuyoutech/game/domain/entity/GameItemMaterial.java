package com.shuyoutech.game.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 */
@Data
public class GameItemMaterial implements Serializable {

    @Schema(description = "游戏物品id")
    private String gameItemId;

    @Schema(description = "游戏物品名称")
    private String gameItemName;

    @Schema(description = "数量")
    @Field(targetType = DECIMAL128)
    private BigDecimal num;

}
