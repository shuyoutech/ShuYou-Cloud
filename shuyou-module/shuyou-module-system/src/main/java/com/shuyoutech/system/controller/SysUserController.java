package com.shuyoutech.system.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.constant.MimeTypeConstants;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.system.domain.bo.SysUserBo;
import com.shuyoutech.system.domain.bo.UserPostsBo;
import com.shuyoutech.system.domain.bo.UserResetPasswordBo;
import com.shuyoutech.system.domain.bo.UserRolesBo;
import com.shuyoutech.system.domain.vo.ProfileUpdatePasswordVo;
import com.shuyoutech.system.domain.vo.ProfileUpdateVo;
import com.shuyoutech.system.domain.vo.ProfileVo;
import com.shuyoutech.system.domain.vo.SysUserVo;
import com.shuyoutech.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.shuyoutech.common.core.model.R.error;
import static com.shuyoutech.common.core.model.R.success;

/**
 * @author YangChao
 * @date 2025-02-14 16:18:57
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Tag(name = "SysUserController", description = "用户管理API控制器")
public class SysUserController {

    @PostMapping("page")
    @Operation(description = "用户分页列表")
    public R<PageResult<SysUserVo>> page(@RequestBody PageQuery<SysUserBo> pageQuery) {
        return R.success(sysUserService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询用户详情")
    public R<SysUserVo> detail(@PathVariable String id) {
        return R.success(sysUserService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增用户")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody SysUserBo bo) {
        return R.success(sysUserService.saveSysUser(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改用户")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody SysUserBo bo) {
        return R.success(sysUserService.updateSysUser(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除用户")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(sysUserService.deleteSysUser(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(sysUserService.checkUnique(param));
    }

    @PostMapping(path = "status")
    @Operation(description = "状态修改")
    public R<Boolean> status(@Validated({StatusGroup.class}) @RequestBody SysUserBo bo) {
        return R.success(sysUserService.statusSysUser(bo.getId(), bo.getStatus()));
    }

    @PostMapping("import")
    @Operation(description = "导入用户")
    public R<JSONObject> importUser(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return R.success(sysUserService.importUser(file, request));
    }

    @PostMapping(path = "export")
    @Operation(description = "导出用户")
    public void export(@RequestBody SysUserBo query, HttpServletRequest request, HttpServletResponse response) {
        sysUserService.export(query, request, response);
    }

    @PostMapping(path = "resetPassword")
    @Operation(description = "重置密码")
    public R<Void> resetPassword(@Validated @RequestBody UserResetPasswordBo bo) {
        sysUserService.resetPassword(bo.getUserId(), bo.getPassword());
        return R.success();
    }

    @PostMapping("grantRole")
    @Operation(description = "用户授权角色")
    public R<Void> grantRole(@Validated @RequestBody UserRolesBo bo) {
        sysUserService.grantRole(bo.getUserId(), bo.getRoleIds());
        return R.success();
    }

    @PostMapping("grantPost")
    @Operation(description = "用户授权岗位")
    public R<Void> grantPost(@Validated @RequestBody UserPostsBo bo) {
        sysUserService.grantPost(bo.getUserId(), bo.getPostIds());
        return R.success();
    }

    @PostMapping("getProfile")
    @Operation(summary = "获取个人信息")
    public R<ProfileVo> getProfile() {
        return R.success(sysUserService.getProfile());
    }

    @PostMapping(path = "updateProfile")
    @Operation(description = "修改个人信息")
    public R<Void> updateProfile(@RequestBody ProfileUpdateVo profile) {
        sysUserService.updateProfile(profile);
        return R.success();
    }

    @PostMapping(path = "updatePassword")
    @Operation(description = "修改密码")
    public R<Void> updatePassword(@RequestBody ProfileUpdatePasswordVo updatePwd) {
        String userId = AuthUtils.getLoginUserId();
        String newPassword = updatePwd.getNewPassword();
        String oldPassword = updatePwd.getOldPassword();
        sysUserService.updatePassword(userId, oldPassword, newPassword);
        return R.success();
    }

    @PostMapping("avatar")
    @Operation(description = "上传用户头像")
    public R<String> avatar(@RequestParam("file") MultipartFile file) {
        try {
            if (null == file) {
                return error("上传文件不能为空");
            }
            String fileType = FileUtil.extName(file.getOriginalFilename());
            if (!ArrayUtil.contains(MimeTypeConstants.IMAGE_EXTENSION, fileType)) {
                return error(StringUtils.format("文件格式不正确，请上传{}格式", Arrays.toString(MimeTypeConstants.IMAGE_EXTENSION)));
            }
            return success(sysUserService.avatar(file));
        } catch (Exception e) {
            log.error("上传用户头像异常 ============ exception:{}", e.getMessage());
        }
        return error("上传用户头像失败");
    }

    @PostMapping("permission")
    @Operation(description = "获取路由权限")
    public R<Set<String>> permission() {
        String userId = AuthUtils.getLoginUserId();
        return R.success(sysUserService.permission(userId));
    }

    private final SysUserService sysUserService;

}
