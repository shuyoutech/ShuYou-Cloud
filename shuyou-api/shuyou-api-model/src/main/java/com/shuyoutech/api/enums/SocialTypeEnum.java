package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 社交平台的类型枚举
 *
 * @author YangChao
 * @date 2025-07-05 20:18
 **/
@Getter
@AllArgsConstructor
public enum SocialTypeEnum implements BaseEnum<String, String> {

    /**
     * 微信开放平台 - 网站应用 PC 端扫码授权登录
     *
     * @see <a href="https://justauth.wiki/guide/oauth/wechat_open/#_2-申请开发者资质认证">接入文档</a>
     */
    WECHAT_OPEN("01", "WECHAT_OPEN"),

    /**
     * 微信小程序
     *
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">接入文档</a>
     */
    WECHAT_MINI_PROGRAM("02", "WECHAT_MINI_PROGRAM"),

    /**
     * 微信公众平台 - 移动端 H5
     *
     * @see <a href="https://www.cnblogs.com/juewuzhe/p/11905461.html">接入文档</a>
     */
    WECHAT_MP("03", "WECHAT_MP"),

    /**
     * 企业微信
     *
     * @see <a href="https://xkcoding.com/2019/08/06/use-justauth-integration-wechat-enterprise.html">接入文档</a>
     */
    WECHAT_ENTERPRISE("04", "WECHAT_ENTERPRISE"),

    /**
     * Gitee
     *
     * @see <a href="https://gitee.com/api/v5/oauth_doc#/">接入文档</a>
     */
    GITEE("10", "GITEE"),

    /**
     * 钉钉
     *
     * @see <a href="https://developers.dingtalk.com/document/app/obtain-identity-credentials">接入文档</a>
     */
    DINGTALK("20", "DINGTALK"),

    ;

    private final String value;
    private final String label;

}
