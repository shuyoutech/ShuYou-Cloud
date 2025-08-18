package com.shuyoutech.member.domain.bo;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.member.domain.entity.SocialUserEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-22 13:29:35
 **/
@Data
@AutoMapper(target = SocialUserEntity.class)
@Schema(description = "社交用户类")
public class SocialUserBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @Schema(description = "社交平台类型")
    private String socialType;

    @Schema(description = "用户openid")
    private String openid;

    @Schema(description = "用户的授权令牌")
    private String accessToken;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "原始用户数据，一般是 JSON 格式")
    private String rawUserInfo;

    @Schema(description = "最后一次的认证 code")
    private String code;

    @Schema(description = "最后一次的认证 state")
    private String state;

}
