package com.shuyoutech.game.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.game.domain.vo.GameTradingPlatformVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GameTradingPlatformVo.class)
@Document(collection = "game_trading_platform")
@Schema(description = "游戏交易平台表类")
public class GameTradingPlatformEntity extends BaseEntity<GameTradingPlatformEntity> {

    @Schema(description = "平台编号")
    private String platformCode;

    @Schema(description = "平台名称")
    private String platformName;

    @Schema(description = "平台简称")
    private String platformAlias;

    @Schema(description = "平台图标")
    private String platformIcon;

    @Schema(description = "平台官网")
    private String platformWebsite;

}
