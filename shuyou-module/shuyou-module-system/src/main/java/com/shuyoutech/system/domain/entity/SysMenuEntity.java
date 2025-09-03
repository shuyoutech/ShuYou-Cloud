package com.shuyoutech.system.domain.entity;

import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.TreeEntity;
import com.shuyoutech.system.domain.vo.SysMenuVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-07 19:53
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysMenuVo.class)
@Schema(description = "菜单表")
@Document(collection = "sys_menu")
public class SysMenuEntity extends TreeEntity<SysMenuEntity> {

    /**
     * 枚举 {@link StatusEnum}
     */
    @Schema(description = "状态:0-停用,1-正常")
    private String status;

    @Schema(description = "菜单类型:1-目录,2-菜单,3-按钮,4-外链")
    private String menuType;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单路径")
    private String menuPath;

    @Schema(description = "菜单排序")
    private Integer menuSort;

    @Schema(description = "菜单描述")
    private String menuDesc;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "是否外链")
    private Boolean beenExt;

    @Schema(description = "是否缓存")
    private Boolean beenKeepalive;

    @Schema(description = "是否显示")
    private Boolean beenVisible;

}
