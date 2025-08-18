package com.shuyoutech.member.socail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:18
 **/
@Data
@Builder
public class SocialClientConfig implements Serializable {

    @Schema(description = "客户端ID,对应各平台的appKey")
    private String clientId;

    @Schema(description = "客户端Secret,对应各平台的appSecret")
    private String clientSecret;

    @Schema(description = "登录成功后的回调地址")
    private String redirectUri;

    @Schema(description = "是否需要申请unionid，目前只针对qq登录")
    private boolean unionId;

    @Schema(description = "企业微信，授权方的网页应用ID")
    private String agentId;

    @Schema(description = "企业微信第三方授权用户类型，member|admin")
    private String usertype;

    @Schema(description = "域名前缀")
    private String domainPrefix;

    @Schema(description = "支持自定义授权平台的 scope 内容")
    private List<String> scopes;

    @Schema(description = "设备ID, 设备唯一标识ID")
    private String deviceId;

}
