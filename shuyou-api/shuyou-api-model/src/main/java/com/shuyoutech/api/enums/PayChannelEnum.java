package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道的编码的枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum PayChannelEnum implements BaseEnum<String, String> {

    WEIXIN_NATIVE("01", "微信Native支付"),

    WEIXIN_MP("02", "微信小程序支付"),

    ;

    private final String value;
    private final String label;

}
