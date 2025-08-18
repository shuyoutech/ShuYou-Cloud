package com.shuyoutech.member.domain.vo;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-22 13:29:35
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "社交用户显示类")
public class SocialUserVo extends BaseVo {

    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @Schema(description = "社交平台类型")
    private Integer socialType;

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
