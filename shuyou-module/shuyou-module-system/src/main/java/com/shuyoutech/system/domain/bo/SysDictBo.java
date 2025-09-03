package com.shuyoutech.system.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.system.domain.entity.SysDictEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-07 10:17:54
 **/
@Data
@AutoMapper(target = SysDictEntity.class)
@Schema(description = "字典类")
public class SysDictBo implements Serializable {

    @NotNull(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "字典排序")
    private Integer dictSort;

    @Schema(description = "字典描述")
    private String dictDesc;

}
