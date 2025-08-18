package com.shuyoutech.member.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-03 09:24
 **/
@Data
public class MemberUserBind implements Serializable {

    @Schema(description = "社交平台的类型")
    private String socialType;

    @Schema(description = "社交平台的用户编号")
    private String socialUserId;

}
