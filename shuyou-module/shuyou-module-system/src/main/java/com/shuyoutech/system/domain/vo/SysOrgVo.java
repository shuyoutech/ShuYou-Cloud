package com.shuyoutech.system.domain.vo;

import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.mongodb.model.TreeVo;
import com.shuyoutech.system.enums.OrgTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-07-07 09:17:49
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "机构显示类")
public class SysOrgVo extends TreeVo {

    /**
     * 枚举 {@link StatusEnum}
     */
    @NotNull(message = "状态不能为空", groups = {StatusGroup.class})
    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态名称")
    private String statusName;

    /**
     * 枚举 {@link OrgTypeEnum}
     */
    @Schema(description = "机构类型")
    private String orgType;

    @Schema(description = "机构类型名称")
    private String orgTypeName;

    @Schema(description = "机构名称")
    private String orgName;

    @Schema(description = "机构排序")
    private Integer orgSort;

    @Schema(description = "负责人ID")
    private String directorId;

    @Schema(description = "负责人名称")
    private String directorName;

}
