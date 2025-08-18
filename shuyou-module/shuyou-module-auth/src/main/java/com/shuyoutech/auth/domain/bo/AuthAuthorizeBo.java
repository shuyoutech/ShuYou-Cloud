package com.shuyoutech.auth.domain.bo;

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
@Schema(description = "社交用户跳转类")
public class AuthAuthorizeBo implements Serializable {

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @NotEmpty(message = "社交平台的类型不能为空")
    @Schema(description = "社交平台的类型，参见 UserSocialTypeEnum 枚举值")
    private String socialType;

}
