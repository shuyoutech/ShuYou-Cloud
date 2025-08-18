package com.shuyoutech.auth.controller;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.auth.domain.bo.*;
import com.shuyoutech.auth.domain.vo.AuthLoginVo;
import com.shuyoutech.auth.service.AuthService;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.annotation.RateLimiter;
import com.shuyoutech.common.web.enums.LimitTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangChao
 * @date 2025-07-19 10:52
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "权限控制器")
public class AuthController {

    @PostMapping("/api/auth/login")
    @Operation(summary = "使用账号密码登录")
    public R<AuthLoginVo> login(@Validated @RequestBody AuthLoginBo req) {
        return R.success(authService.login(req));
    }

    @PostMapping("/api/member/auth/authorize")
    @Operation(summary = "社交用户授权,获取code")
    public R<String> authorize(@Validated @RequestBody AuthAuthorizeBo bo) {
        return R.success(authService.authorize(bo));
    }

    @PostMapping("/api/member/auth/accessToken")
    @Operation(summary = "根据code获取第三方访问令牌")
    public R<AuthLoginVo> accessToken(@Validated @RequestBody AuthAccessToken bo) {
        return R.success(authService.accessToken(bo));
    }

    @RateLimiter(key = "SmsSend", count = 1, limitType = LimitTypeEnum.IP)
    @PostMapping("/api/member/auth/sendSms")
    @Operation(summary = "发送手机验证码")
    public R<Boolean> sendSms(@RequestBody @Valid SmsSendBo bo) {
        authService.sendSms(bo);
        return R.success(true);
    }

    @PostMapping("/api/member/auth/smsLogin")
    @Operation(summary = "使用手机 + 验证码登录")
    public R<AuthLoginVo> smsLogin(@Validated @RequestBody SmsLoginBo bo) {
        return R.success(authService.smsLogin(bo));
    }

    @PostMapping("/auth/logout")
    @Operation(description = "用户退出")
    public R<Void> logout() {
        authService.logout();
        return R.success();
    }

    private final AuthService authService;

}
