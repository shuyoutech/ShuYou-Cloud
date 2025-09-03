package com.shuyoutech.member.domain.vo;

import com.shuyoutech.common.core.enums.SexEnum;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import com.shuyoutech.member.domain.entity.MemberUserBind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-01 15:16:55
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "会员用户显示类")
public class MemberUserVo extends BaseVo {

    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 枚举 {@link StatusEnum}
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 枚举 {@link SexEnum}
     */
    @Schema(description = "性别")
    private String sex;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "社交用户的绑定")
    private List<MemberUserBind> binds;

}
