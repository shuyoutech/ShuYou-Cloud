package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YangChao
 * @date 2025-07-10 14:45
 **/
@Getter
@AllArgsConstructor
@Schema(description = "游戏枚举")
public enum GameEnum implements BaseEnum<String, String> {

    Game_AION("1666459521037029392", "永恒之塔"),

    GAME_LOSTARK("1666459521037029393", "命运方舟"),

    Game_DNF("1667409850943606784", "地下城与勇士");

    private final String value;
    private final String label;

}
