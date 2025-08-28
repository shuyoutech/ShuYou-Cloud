package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏商品出售模式枚举
 *
 * @author YangChao
 * @date 2025-07-05 20:18
 **/
@Getter
@AllArgsConstructor
public enum SellTypeEnum implements BaseEnum<String, String> {

    CONSIGNMENT("1", "寄售"),

    GUARANTEE("2", "担保");

    private final String value;
    private final String label;

}
