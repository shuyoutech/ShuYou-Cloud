package com.shuyoutech.member.socail;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.SocialClientToken;
import com.shuyoutech.api.model.SocialUserInfo;

/**
 * @author YangChao
 * @date 2025-07-06 14:54
 **/
public interface SocialClientRequest {

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state          验证授权流程的参数，可以防止csrf
     * @param callBackSuffix 回调地址后缀
     * @return 返回授权地址
     */
    String authorize(String state, String callBackSuffix);

    /**
     * 获取access token
     *
     * @param code 授权成功后的回调参数
     * @return token
     */
    SocialClientToken getAccessToken(String code);

    /**
     * 根据token获取用户信息
     *
     * @param socialClientToken token信息
     * @return 用户信息
     */
    SocialUserInfo getUserInfo(SocialClientToken socialClientToken);

    /**
     * 第三方登录
     *
     * @param bo 用于接收回调参数的实体
     * @return 返回登录成功后的用户信息
     */
    SocialUserInfo login(AuthAccessToken bo);

    /**
     * 刷新access token
     *
     * @param socialClientToken 登录成功后返回的Token信息
     * @return token
     */
    SocialClientToken refreshToken(SocialClientToken socialClientToken);
}
