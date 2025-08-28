package com.shuyoutech.bbs.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "游戏商城显示类")
public class GameShopVo extends BaseVo {

    @Schema(description = "商城名称")
    private String shopName;

    @Schema(description = "商城简称")
    private String shopAlias;

    @Schema(description = "商城图标")
    private String shopIcon;

    @Schema(description = "商城地址")
    private String shopUrl;

}
