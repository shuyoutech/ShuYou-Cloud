package com.shuyoutech.notice.service;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.shuyoutech.notice.domain.bo.NoticeSendBo;

/**
 * @author YangChao
 * @date 2025-07-19 14:00
 **/
public interface NoticeSmsService {

    /**
     * 处理通知消息
     *
     * @param bo 通知请求对象
     */
    void handler(NoticeSendBo bo);

    /**
     * 发送手机验证码
     *a
     * @param mobile       手机号
     * @param code         验证码
     * @param templateCode 模版id
     */
    void sendSms(String mobile, String code, String templateCode);

    /**
     * 短信发送
     *
     * @param request 短信发送对象
     */
    void sendSms(SendSmsRequest request);

    /**
     * 校验手机验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return boolean
     */
    boolean validateSmsCode(String mobile, String code);

    /**
     * 清除验证码
     *
     * @param mobile 手机号
     */
    void cleanSmsCode(String mobile);

}
