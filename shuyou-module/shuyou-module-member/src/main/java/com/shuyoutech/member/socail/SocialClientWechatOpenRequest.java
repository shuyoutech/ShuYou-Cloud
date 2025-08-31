package com.shuyoutech.member.socail;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.SocialClientToken;
import com.shuyoutech.api.model.SocialUserInfo;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 微信开放平台 网站应用 <a href="https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html">...</a>
 *
 * @author YangChao
 * @date 2025-07-10 09:59
 **/
@Slf4j
public class SocialClientWechatOpenRequest implements SocialClientRequest {

    protected SocialClientConfig socialClient;

    public SocialClientWechatOpenRequest(SocialClientConfig socialClient) {
        this.socialClient = socialClient;
    }

    @Override
    public String authorize(String state, String callBackSuffix) {
        // 网站应用微信登录 https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
        Map<String, Object> paramMap = MapUtil.newHashMap();
        // 微信开放平台 appId
        paramMap.put("appid", socialClient.getClientId());
        // 重定向地址 请使用urlEncode对链接进行处理
        paramMap.put("redirect_uri", socialClient.getRedirectUri() + callBackSuffix);
        // 响应类型 填code
        paramMap.put("response_type", "code");
        // 应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login
        paramMap.put("scope", "snsapi_login");
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        paramMap.put("state", state);
        // 样式
        paramMap.put("href", "https://www.shuyoutech.com/preview/custom.css");
        String authorizeUrl = HttpClientUtils.packageUrl("https://open.weixin.qq.com/connect/qrconnect", paramMap) + "#wechat_redirect";
        log.info("authorize ========== url:{}", authorizeUrl);
        return authorizeUrl;
    }

    @Override
    public SocialClientToken getAccessToken(String code) {
        // 通过code获取access_token https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
        // 使用 code 获取对应的 openId、unionId 等字段
        Map<String, Object> paramMap = MapUtil.newHashMap();
        // 小程序 appId
        paramMap.put("appid", socialClient.getClientId());
        // 小程序 appSecret
        paramMap.put("secret", socialClient.getClientSecret());
        // 登录时获取的 code，可通过wx.login获取
        paramMap.put("code", code);
        // 授权类型，此处只需填写 authorization_code
        paramMap.put("grant_type", "authorization_code");
        log.info("getAccessToken ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用微信登录接口异常");
        }
        log.info("getAccessToken ========== res:{}", result.toJSONString());
        int errcode = result.getIntValue("errcode", 0);
        if (0 != errcode) {
            throw new BusinessException("微信登录接口失败:" + errcode + "-" + result.getString("errmsg"));
        }
        return SocialClientToken.builder() //
                .openId(result.getString("openid")) //
                .unionId(result.getString("unionid")) //
                .accessToken(result.getString("access_token")) //
                .expireIn(result.getIntValue("expires_in")) //
                .refreshToken(result.getString("refresh_token")) //
                .scope(result.getString("scope")) //
                .build();
    }

    @Override
    public SocialUserInfo getUserInfo(SocialClientToken token) {
        // 获取用户个人信息 https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Authorized_Interface_Calling_UnionID.html
        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("access_token", token.getAccessToken());
        paramMap.put("openid", token.getOpenId());
        log.info("getUserInfo ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendGet("https://api.weixin.qq.com/sns/userinfo", paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用微信获取用户个人信息接口异常");
        }
        log.info("getUserInfo ========== res:{}", result.toJSONString());
        int errcode = result.getIntValue("errcode", 0);
        if (0 != errcode) {
            throw new BusinessException("微信获取用户个人信息接口失败:" + errcode + "-" + result.getString("errmsg"));
        }
        return SocialUserInfo.builder() //
                .socialType(SocialTypeEnum.WECHAT_OPEN.getValue()) //
                .nickname(result.getString("nickname")) //
                .avatar(result.getString("headimgurl")) //
                .sex(result.getString("sex")) //
                .location(String.format("%s-%s-%s", result.getString("country"), result.getString("province"), result.getString("city"))) //
                .token(token) //
                .build();
    }

    @Override
    public SocialUserInfo login(AuthAccessToken bo) {
        SocialClientToken token = getAccessToken(bo.getCode());
        return getUserInfo(token);
    }

    @Override
    public SocialClientToken refreshToken(SocialClientToken socialClientToken) {
        // 通过code获取access_token https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
        // 使用 code 获取对应的 openId、unionId 等字段
        Map<String, Object> paramMap = MapUtil.newHashMap();
        // 小程序 appId
        paramMap.put("appid", socialClient.getClientId());
        // 填写通过access_token获取到的refresh_token参数
        paramMap.put("refresh_token", socialClientToken.getRefreshToken());
        // 授权类型，此处只填refresh_token
        paramMap.put("grant_type", "refresh_token");
        log.info("refreshToken ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendGet("https://api.weixin.qq.com/sns/oauth2/refresh_token", paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用微信刷新token异常");
        }
        log.info("refreshToken ========== res:{}", result.toJSONString());
        int errcode = result.getIntValue("errcode", 0);
        if (0 != errcode) {
            throw new BusinessException("微信刷新token接口失败:" + errcode + "-" + result.getString("errmsg"));
        }
        return SocialClientToken.builder() //
                .openId(result.getString("openid")) //
                .accessToken(result.getString("access_token")) //
                .expireIn(result.getIntValue("expires_in")) //
                .refreshToken(result.getString("refresh_token")) //
                .scope(result.getString("scope")) //
                .build();
    }

}
