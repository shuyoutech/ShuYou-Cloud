package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YangChao
 * @date 2025-07-07 13:16
 **/
@Getter
@AllArgsConstructor
@Schema(description = "游戏商品类型枚举")
public enum GameGoodsTypeEnum implements BaseEnum<String, String> {

    GOODS_TYPE_ZH("ZH", "账号"),

    GOODS_TYPE_YXB("YXB", "游戏币");

    private final String value;
    private final String label;

}
