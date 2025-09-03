package com.shuyoutech.system.domain.vo;

import com.shuyoutech.common.mongodb.model.TreeVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-06-28 11:03:12
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "字典显示类")
public class SysDictVo extends TreeVo {

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
