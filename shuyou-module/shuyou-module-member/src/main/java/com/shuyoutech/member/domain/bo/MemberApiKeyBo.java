package com.shuyoutech.member.domain.bo;

import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.member.domain.entity.MemberApiKeyEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-19 16:57:40
 **/
@Data
@AutoMapper(target = MemberApiKeyEntity.class)
@Schema(description = "用户API key类")
public class MemberApiKeyBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户ID")
    private String userId;

    @NotBlank(message = "名称不能为空", groups = {SaveGroup.class, UpdateGroup.class})
    @Schema(description = "名称")
    private String apiName;

    @Schema(description = "Key")
    private String apiKey;

}
