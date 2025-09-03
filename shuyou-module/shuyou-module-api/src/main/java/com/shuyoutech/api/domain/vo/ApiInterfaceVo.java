package com.shuyoutech.api.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "api接口显示类")
public class ApiInterfaceVo extends BaseVo {

    @Schema(description = "接口名称")
    private String interfaceName;

    @Schema(description = "接口编号")
    private String interfaceCode;

    @Schema(description = "接口实现类")
    private String interfaceClass;

    @Schema(description = "接口描述")
    private String interfaceDesc;

}
