package com.shuyoutech.api.domain.entity;

import com.shuyoutech.api.domain.vo.ApiInterfaceVo;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ApiInterfaceVo.class)
@Document(collection = "api_interface")
@Schema(description = "api接口表类")
public class ApiInterfaceEntity extends BaseEntity<ApiInterfaceEntity> {

    @Schema(description = "接口名称")
    private String interfaceName;

    @Schema(description = "接口编号")
    private String interfaceCode;

    @Schema(description = "接口实现类")
    private String interfaceClass;

    @Schema(description = "接口描述")
    private String interfaceDesc;

}
