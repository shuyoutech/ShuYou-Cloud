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
@Schema(description = "游戏平台枚举")
public enum GamePlatformEnum implements BaseEnum<String, String> {

    PLATFORM_5173("1666820302429655040", "5173"),

    PLATFORM_GMM("1666820302429655041", "G买卖"),

    PLATFORM_UU898("1666820302429655042", "UU898"),

    PLATFORM_DD373("1666820302429655043", "DD373"),

    PLATFORM_7881("1666820302429655044", "7881");

    private final String value;
    private final String label;

}
