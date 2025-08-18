package com.shuyoutech.member.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:07
 **/
@Data
@Schema(description = "手机验证码登录类")
public class SmsLoginBo implements Serializable {

    @NotEmpty(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String mobile;

    @NotEmpty(message = "手机验证码不能为空")
    @Schema(description = "手机验证码")
    private String code;

}
