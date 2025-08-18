package com.shuyoutech.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-06 14:59
 **/
@Data
@Builder
public class SocialClientToken implements Serializable {

    @Schema(description = "授权令牌")
    private String accessToken;

    @Schema(description = "令牌有效时间，单位为秒")
    private int expireIn;

    @Schema(description = "刷新新令牌")
    private String refreshToken;

    @Schema(description = "刷新令牌的有效时间")
    private int refreshTokenExpireIn;

    @Schema(description = "商家的UID")
    private String uid;

    @Schema(description = "用户唯一标识")
    private String openId;

    @Schema(description = "")
    private String accessCode;

    @Schema(description = "用户在开放平台的唯一标识符")
    private String unionId;

    @Schema(description = "Google附带属性")
    private String scope;
    private String tokenType;
    private String idToken;

    @Schema(description = "小米附带属性")
    private String macAlgorithm;
    private String macKey;

    @Schema(description = "企业微信附带属性")
    private String code;

    @Schema(description = "微信公众号 - 网页授权的登录时可用")
    private boolean snapshotUser;

    @Schema(description = "Twitter附带属性")
    private String oauthToken;
    private String oauthTokenSecret;
    private String userId;
    private String screenName;
    private Boolean oauthCallbackConfirmed;

    @Schema(description = "Apple附带属性")
    private String username;

    @Schema(description = "新版钉钉附带属性")
    private String corpId;

}
