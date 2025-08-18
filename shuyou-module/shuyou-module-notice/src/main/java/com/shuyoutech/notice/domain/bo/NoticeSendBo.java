package com.shuyoutech.notice.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Data
@Schema(description = "通知发送请求类")
public class NoticeSendBo implements Serializable {

    @NotEmpty(message = "通知类型不能为空")
    @Schema(description = "通知类型，可选值：sms,email")
    private String type;

    @NotEmpty(message = "模板ID不能为空")
    @Schema(description = "模板ID")
    private String templateId;

    @NotEmpty(message = "通知地址不能为空")
    @Schema(description = "根据通知类型，对应的手机号或者邮箱地址")
    private String sendAddr;

    @Schema(description = "根据模板内容，模板的参数值")
    private Map<String, Object> params;

    @Schema(description = "根据模版和参数转化内容")
    private String content;

}
