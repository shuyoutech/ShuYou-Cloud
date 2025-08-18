package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知状态枚举
 *1-通知中,2-通知成功,3-通知失败
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum PayNotifyStatusEnum implements BaseEnum<String, String> {

    INIT("0", "通知中"),

    SUCCESS("1", "通知成功"),

    FAILED("2", "通知失败");

    private final String value;
    private final String label;

}
