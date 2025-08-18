package com.shuyoutech.auth.service;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.auth.domain.bo.*;
import com.shuyoutech.auth.domain.vo.AuthLoginVo;
import com.shuyoutech.common.satoken.model.LoginUser;

/**
 * @author YangChao
 * @date 2025-07-19 11:13
 **/
public interface AuthService {

    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    LoginUser authenticate(String username, String password);

    /**
     * 账号登录
     *
     * @param req 登录信息
     * @return 登录结果
     */
    AuthLoginVo login(AuthLoginBo req);

    /**
     * 手机 + 验证码登录
     *
     * @param bo 登录信息
     * @return 登录结果
     */
    AuthLoginVo smsLogin(SmsLoginBo bo);

    /**
     * 发送手机验证码
     *
     * @param bo 发送信息
     */
    void sendSms(SmsSendBo bo);

    /**
     * 获得社交认证 URL
     *
     * @param bo 跳转请求信息
     * @return 认证 URL
     */
    String authorize(AuthAuthorizeBo bo);

    /**
     * 社交快捷登录，使用 code 授权码
     *
     * @param bo 登录信息
     * @return 登录结果
     */
    AuthLoginVo accessToken(AuthAccessToken bo);

    /**
     * 退出登录
     */
    void logout();

}
