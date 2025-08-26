package com.shuyoutech.api.domain.bo;

import com.shuyoutech.api.domain.entity.ApiInterfaceEntity;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
@Data
@AutoMapper(target = ApiInterfaceEntity.class)
@Schema(description = "api接口类")
public class ApiInterfaceBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "接口名称")
    private String interfaceName;

    @Schema(description = "接口编号")
    private String interfaceCode;

    @Schema(description = "接口实现类")
    private String interfaceClass;

    @Schema(description = "接口描述")
    private String interfaceDesc;

}
