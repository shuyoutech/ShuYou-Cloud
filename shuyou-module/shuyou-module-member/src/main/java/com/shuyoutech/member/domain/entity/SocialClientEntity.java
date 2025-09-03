package com.shuyoutech.member.domain.entity;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.api.model.RemoteSysSocialClient;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.member.domain.vo.SocialClientVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-07-07 00:04
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMappers({@AutoMapper(target = RemoteSysSocialClient.class), @AutoMapper(target = SocialClientVo.class)})
@Schema(description = "社交客户端表")
@Document(collection = "social_client")
public class SocialClientEntity extends BaseEntity<SocialClientEntity> {

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
