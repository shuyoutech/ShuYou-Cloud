package com.shuyoutech.auth.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:07
 **/
@Data
@Schema(description = "发送手机验证码类")
public class SmsSendBo implements Serializable {

    @NotEmpty(message = "模版编号不能为空")
    @Schema(description = "模版编号")
    private String templateCode;

    @NotEmpty(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String mobile;

}
