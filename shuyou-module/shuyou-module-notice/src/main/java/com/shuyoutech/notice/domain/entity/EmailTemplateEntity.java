package com.shuyoutech.notice.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.notice.domain.vo.EmailTemplateVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = EmailTemplateVo.class)
@Document(collection = "notice_email_template")
@Schema(description = "电子邮件模板表类")
public class EmailTemplateEntity extends BaseEntity<EmailTemplateEntity> {

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "邮件正文")
    private String content;

}
