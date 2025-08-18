package com.shuyoutech.notice.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "短信模板显示类")
public class SmsTemplateVo extends BaseVo {

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "短信签名名称")
    private String signName;

    @Schema(description = "短信模板")
    private String templateCode;

    @Schema(description = "模板内容")
    private String templateParam;

}
