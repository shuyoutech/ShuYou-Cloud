package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单状态枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum PayOrderStatusEnum implements BaseEnum<String, String> {

    GENERATE("0", "订单生成"),

    PAYING("1", "支付中"),

    SUCCESS("2", "支付成功"),

    FAILED("3", "支付失败"),

    REVOKED("4", "已撤销"),

    REFUND("5", "已退款"),

    CLOSE("6", "订单关闭");

    private final String value;
    private final String label;

}
