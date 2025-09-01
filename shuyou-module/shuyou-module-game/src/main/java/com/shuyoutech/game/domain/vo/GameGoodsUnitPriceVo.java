package com.shuyoutech.game.domain.vo;

import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 14:09
 **/
@Data
public class GameGoodsUnitPriceVo implements Serializable {

    @Schema(description = "游戏区服")
    private String areaServer;

    @Schema(description = "商品列表")
    private List<GameGoodsEntity> goodsList;

}
