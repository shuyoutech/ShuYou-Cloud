package com.shuyoutech.api.model;

import com.shuyoutech.api.enums.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-21 16:59:07
 **/
@Data
@Schema(description = "社交客户端类")
public class RemoteSocialClient implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "状态")
    private String status;

    /**
     * 枚举 {@link SocialTypeEnum}
     */
    @Schema(description = "社交平台类型")
    private String socialType;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端ID")
    private String clientId;

    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "回调地址")
    private String redirectUri;

}
