package com.shuyoutech.member.domain.entity;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.member.domain.vo.SocialUserVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-06-11 09:55
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SocialUserVo.class)
@Schema(description = "社交用户表")
@Document(collection = "social_user")
public class SocialUserEntity extends BaseEntity<SocialUserEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

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

}
