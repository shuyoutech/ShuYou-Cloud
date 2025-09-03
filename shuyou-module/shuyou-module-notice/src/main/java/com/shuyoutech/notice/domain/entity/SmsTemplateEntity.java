package com.shuyoutech.notice.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.notice.domain.vo.SmsTemplateVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SmsTemplateVo.class)
@Document(collection = "notice_sms_template")
@Schema(description = "短信模板表类")
public class SmsTemplateEntity extends BaseEntity<SmsTemplateEntity> {

    @Schema(description = "场景编号")
    private String sceneCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "短信签名名称")
    private String signName;

    @Schema(description = "短信模板")
    private String templateCode;

    @Schema(description = "模板内容")
    private String templateParam;

}
