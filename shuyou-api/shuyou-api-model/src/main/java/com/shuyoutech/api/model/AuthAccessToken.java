package com.shuyoutech.api.model;

import com.shuyoutech.api.enums.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:07
 **/
@Data
@Schema(description = "社交用户登录类")
public class AuthAccessToken implements Serializable {

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @NotEmpty(message = "社交平台类型不能为空")
    @Schema(description = "社交平台类型")
    private String socialType;

    @NotEmpty(message = "授权码不能为空")
    @Schema(description = "授权码")
    private String code;

    @Schema(description = "state")
    private String state;

}
