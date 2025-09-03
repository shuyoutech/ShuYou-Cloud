package com.shuyoutech.notice.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "电子邮件模板显示类")
public class EmailTemplateVo extends BaseVo {

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "邮件正文")
    private String content;

}
