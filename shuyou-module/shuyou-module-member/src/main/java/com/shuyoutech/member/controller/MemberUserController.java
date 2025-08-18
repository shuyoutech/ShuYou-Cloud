package com.shuyoutech.member.controller;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.member.domain.bo.MemberUserBo;
import com.shuyoutech.member.domain.bo.SmsLoginBo;
import com.shuyoutech.member.domain.entity.MemberUserEntity;
import com.shuyoutech.member.domain.vo.MemberUserVo;
import com.shuyoutech.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-01 15:16:55
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("memberUser")
@Tag(name = "MemberUserController", description = "会员用户管理API控制器")
public class MemberUserController {

    @PostMapping("page")
    @Operation(description = "会员用户分页列表")
    public R<PageResult<MemberUserVo>> page(@RequestBody PageQuery<MemberUserBo> pageQuery) {
        return R.success(memberUserService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询会员用户详情")
    public R<MemberUserVo> detail(@PathVariable String id) {
        return R.success(memberUserService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增会员用户")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody MemberUserBo bo) {
        return R.success(memberUserService.saveClientUser(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改会员用户")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody MemberUserBo bo) {
        return R.success(memberUserService.updateClientUser(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除会员用户")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(memberUserService.deleteClientUser(ids));
    }

    @PostMapping("getProfile")
    @Operation(summary = "获取个人信息")
    public R<MemberUserEntity> getProfile() {
        return R.success(memberUserService.getMemberProfile());
    }

    @PostMapping(path = "socialUserBind")
    @Operation(description = "绑定社交用户")
    public R<Boolean> socialUserBind(@Validated @RequestBody AuthAccessToken bo) {
        memberUserService.socialUserBind(bo);
        return R.success(true);
    }

    @PostMapping(path = "mobileBind")
    @Operation(description = "绑定手机号")
    public R<Boolean> mobileBind(@Validated @RequestBody SmsLoginBo bo) {
        memberUserService.mobileBind(bo);
        return R.success(true);
    }

    private final MemberUserService memberUserService;

}
