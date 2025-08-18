package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付交易类型枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum PayTradeTypeEnum implements BaseEnum<String, String> {

    JSAPI("JSAPI", "公众号支付"),

    NATIVE("NATIVE", "扫码支付"),

    APP("APP", "APP支付"),

    MICROPAY("MICROPAY", "付款码支付"),

    MWEB("MWEB", "H5支付"),

    FACEPAY("FACEPAY", "刷脸支付");

    private final String value;
    private final String label;

}
