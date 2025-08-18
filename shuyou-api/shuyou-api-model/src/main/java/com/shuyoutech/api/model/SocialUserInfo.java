package com.shuyoutech.api.model;

import com.shuyoutech.api.enums.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-06 14:55
 **/
@Data
@Builder
public class SocialUserInfo implements Serializable {

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @Schema(description = "社交平台类型")
    private String socialType;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "位置")
    private String location;

    @Schema(description = "用户授权的token信息")
    private SocialClientToken token;

}
