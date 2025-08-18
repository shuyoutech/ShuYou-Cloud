package com.shuyoutech.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 11:11
 **/
@Data
@Builder
public class AuthLoginVo implements Serializable {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "过期时间")
    private Long expireTime;

}
