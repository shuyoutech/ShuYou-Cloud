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
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.redis.constant.CacheConstants;
import com.shuyoutech.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://mp.weixin.qq.com/">...</a>
 *
 * @author YangChao
 * @date 2025-07-10 09:59
 **/
@Slf4j
public class SocialClientWechatMiniProgramRequest implements SocialClientRequest {

    protected SocialClientConfig socialClient;

    public SocialClientWechatMiniProgramRequest(SocialClientConfig socialClient) {
        this.socialClient = socialClient;
    }

    @Override
    public String authorize(String state, String callBackSuffix) {
        return "";
    }

    @Override
    public SocialClientToken getAccessToken(String code) {
        // 小程序登录 https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
        // 使用 code 获取对应的 openId、unionId 等字段
        Map<String, Object> paramMap = MapUtil.newHashMap();
        // 小程序 appId
        paramMap.put("appid", socialClient.getClientId());
        // 小程序 appSecret
        paramMap.put("secret", socialClient.getClientSecret());
        // 登录时获取的 code，可通过wx.login获取
        paramMap.put("js_code", code);
        // 授权类型，此处只需填写 authorization_code
        paramMap.put("grant_type", "authorization_code");
        log.info("getAccessToken ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendGet("https://api.weixin.qq.com/sns/jscode2session", paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用小程序登录接口异常");
        }
        log.info("getAccessToken ========== res:{}", result.toJSONString());
        int errcode = result.getIntValue("errcode", 0);
        if (0 != errcode) {
            throw new BusinessException("小程序登录接口失败:" + errcode + "-" + result.getString("errmsg"));
        }
        return SocialClientToken.builder() //
                .openId(result.getString("openid")) //
                .unionId(result.getString("unionid")) //
                .accessToken(result.getString("session_key")) //
                .build();
    }

    @Override
    public SocialUserInfo getUserInfo(SocialClientToken token) {
        // 获取手机号 https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
        String accessToken = getApiToken();

        Map<String, Object> urlMap = MapUtil.newHashMap();
        urlMap.put("access_token", accessToken);

        String url = HttpClientUtils.packageUrl("https://api.weixin.qq.com/wxa/business/getuserphonenumber", urlMap);

        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("code", token.getCode());

        log.info("getUserInfo ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendPost(url, paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用小程序获取手机号接口异常");
        }
        log.info("getUserInfo ========== res:{}", result.toJSONString());
        int errcode = result.getIntValue("errcode", 0);
        if (0 != errcode) {
            throw new BusinessException("小程序获取手机号接口失败:" + errcode + "-" + result.getString("errmsg"));
        }
        JSONObject phoneInfo = result.getJSONObject("phone_info");
        if (null == phoneInfo) {
            throw new BusinessException("调用小程序获取手机号接口失败");
        }
        // phoneInfo {用户绑定的手机号（国外手机号会有区号） phoneNumber  没有区号的手机号 purePhoneNumber 区号 countryCode}
        return SocialUserInfo.builder() //
                .socialType(SocialTypeEnum.WECHAT_MINI_PROGRAM.getValue()) //
                .nickname(phoneInfo.getString("purePhoneNumber")) //
                .mobile(phoneInfo.getString("purePhoneNumber")) //
                .token(token) //
                .build();
    }

    @Override
    public SocialUserInfo login(AuthAccessToken bo) {
        SocialClientToken accessToken = getAccessToken(bo.getCode());
        return getUserInfo(accessToken);
    }

    @Override
    public SocialClientToken refreshToken(SocialClientToken socialClientToken) {
        return null;
    }

    private String getApiToken() {
        // 获取接口调用凭据 https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
        String token = RedisUtils.getString(CacheConstants.MINI_PROGRAM_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Map<String, Object> paramMap = MapUtil.newHashMap();
        // 小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
        paramMap.put("appid", socialClient.getClientId());
        // 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
        paramMap.put("secret", socialClient.getClientSecret());
        // 授权类型，此处只需填写 client_credential
        paramMap.put("grant_type", "client_credential");
        log.info("getApiToken ========== req:{}", JSON.toJSONString(paramMap));
        JSONObject result = HttpClientUtils.sendGet("https://api.weixin.qq.com/cgi-bin/token", paramMap, JSONObject.class);
        if (null == result) {
            throw new BusinessException("调用小程序凭据失败!");
        }
        log.info("getApiToken ========== res:{}", result.toJSONString());
        String accessToken = result.getString("access_token");
        long expiresIn = result.getLongValue("expires_in", 7200L);
        if (StringUtils.isEmpty(accessToken)) {
            throw new BusinessException("调用小程序凭据失败!");
        }
        RedisUtils.set(CacheConstants.MINI_PROGRAM_TOKEN_KEY, accessToken, expiresIn, TimeUnit.SECONDS);
        return accessToken;
    }

}
