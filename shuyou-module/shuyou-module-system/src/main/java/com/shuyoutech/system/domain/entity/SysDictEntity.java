package com.shuyoutech.system.domain.entity;

import com.shuyoutech.common.mongodb.model.TreeEntity;
import com.shuyoutech.system.domain.vo.SysDictTypeVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-07 19:51
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysDictTypeVo.class)
@Schema(description = "字典表")
@Document(collection = "sys_dict")
public class SysDictEntity extends TreeEntity<SysDictEntity> {

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
