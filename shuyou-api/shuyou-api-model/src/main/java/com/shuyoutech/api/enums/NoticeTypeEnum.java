package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YangChao
 * @date 2025-07-07 08:58
 **/
@Getter
@AllArgsConstructor
public enum NoticeTypeEnum implements BaseEnum<String, String> {

    SMS("sms", "手机短信"),

    EMAIL("email", "电子邮件");

    private final String value;
    private final String label;

}
