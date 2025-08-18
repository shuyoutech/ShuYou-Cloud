package com.shuyoutech.auth.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:07
 **/
@Data
@Schema(description = "账号密码登录类")
public class AuthLoginBo implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 4, max = 16, message = "账号长度为 4-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "用户名格式为数字以及字母")
    @Schema(description = "用户名")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    @Schema(description = "密码")
    private String password;

}
