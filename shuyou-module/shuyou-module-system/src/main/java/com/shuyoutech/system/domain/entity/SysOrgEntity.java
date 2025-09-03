package com.shuyoutech.system.domain.entity;

import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.TreeEntity;
import com.shuyoutech.system.domain.vo.SysOrgVo;
import com.shuyoutech.system.enums.OrgTypeEnum;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-07 19:49
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOrgVo.class)
@Schema(description = "机构表")
@Document(collection = "sys_org")
public class SysOrgEntity extends TreeEntity<SysOrgEntity> {

    /**
     * 枚举 {@link StatusEnum}
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 枚举 {@link OrgTypeEnum}
     */
    @Schema(description = "机构类型")
    private String orgType;

    @Schema(description = "机构名称")
    private String orgName;

    @Schema(description = "机构排序")
    private Integer orgSort;

    @Schema(description = "负责人ID")
    private String directorId;

}
