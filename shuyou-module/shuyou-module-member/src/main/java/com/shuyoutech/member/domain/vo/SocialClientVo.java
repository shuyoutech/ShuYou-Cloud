package com.shuyoutech.member.domain.vo;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-07-25 10:39:30
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "社交客户端显示类")
public class SocialClientVo extends BaseVo {

    /**
     * 枚举 {@link StatusEnum}
     */
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
