package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.GameShopEntity;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Data
@AutoMapper(target = GameShopEntity.class)
@Schema(description = "游戏商城类")
public class GameShopBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "商城名称")
    private String shopName;

    @Schema(description = "商城简称")
    private String shopAlias;

    @Schema(description = "商城图标")
    private String shopIcon;

    @Schema(description = "商城地址")
    private String shopUrl;

}
