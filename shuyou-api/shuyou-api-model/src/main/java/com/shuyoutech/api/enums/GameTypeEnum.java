package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏类型枚举
 *
 * @author YangChao
 * @date 2025-07-05 20:18
 **/
@Getter
@AllArgsConstructor
public enum GameTypeEnum implements BaseEnum<String, String> {

    ONLINE_GAME("1", "网络游戏"),

    MOBILE_GAME("2", "手机游戏"),

    WEB_GAME("3", "网页游戏");

    private final String value;
    private final String label;

}
