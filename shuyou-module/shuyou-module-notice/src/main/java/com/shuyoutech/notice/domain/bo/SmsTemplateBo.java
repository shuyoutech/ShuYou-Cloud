package com.shuyoutech.notice.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.notice.domain.entity.SmsTemplateEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Data
@AutoMapper(target = SmsTemplateEntity.class)
@Schema(description = "短信模板类")
public class SmsTemplateBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "短信签名名称")
    private String signName;

    @Schema(description = "短信模板")
    private String templateCode;

    @Schema(description = "模板内容")
    private String templateParam;

}
