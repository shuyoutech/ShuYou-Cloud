package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 渠道的退款状态枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum implements BaseEnum<String, String> {

    REFUNDING("1", "退款中"),

    SUCCESS("2", "退款成功"),

    FAILED("3", "退款失败");

    private final String value;
    private final String label;

}
