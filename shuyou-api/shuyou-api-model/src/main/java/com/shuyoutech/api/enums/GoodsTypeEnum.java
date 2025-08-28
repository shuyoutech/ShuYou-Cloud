package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏商品类型枚举
 *
 * @author YangChao
 * @date 2025-07-05 20:18
 **/
@Getter
@AllArgsConstructor
public enum GoodsTypeEnum implements BaseEnum<String, String> {

    ZH("1", "账号"),

    YXB("2", "游戏币"),

    ZB("3", "装备"),

    DQ("4", "点券");

    private final String value;
    private final String label;

}
