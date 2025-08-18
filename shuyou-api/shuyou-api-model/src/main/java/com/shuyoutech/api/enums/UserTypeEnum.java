package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author YangChao
 * @date 2025-07-05 20:18
 **/
@Getter
@AllArgsConstructor
public enum UserTypeEnum implements BaseEnum<String, String> {

    MEMBER("member", "会员"),

    ADMIN("admin", "后台管理员");

    private final String value;
    private final String label;

}
