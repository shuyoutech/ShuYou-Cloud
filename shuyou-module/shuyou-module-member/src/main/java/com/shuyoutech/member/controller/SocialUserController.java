package com.shuyoutech.member.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.member.domain.bo.SocialUserBo;
import com.shuyoutech.member.domain.vo.SocialUserVo;
import com.shuyoutech.member.service.SocialUserService;
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
 * @date 2025-07-21 16:59:07
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("socialUser")
@Tag(name = "SysSocialUserController", description = "社交用户管理API控制器")
public class SocialUserController {

    @PostMapping("page")
    @Operation(description = "社交用户分页列表")
    public R<PageResult<SocialUserVo>> page(@RequestBody PageQuery<SocialUserBo> pageQuery) {
        return R.success(socialUserService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询社交用户详情")
    public R<SocialUserVo> detail(@PathVariable String id) {
        return R.success(socialUserService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增社交用户")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody SocialUserBo bo) {
        return R.success(socialUserService.saveSysSocialUser(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改社交用户")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody SocialUserBo bo) {
        return R.success(socialUserService.updateSysSocialUser(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除社交用户")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(socialUserService.deleteSysSocialUser(ids));
    }

    private final SocialUserService socialUserService;

}
