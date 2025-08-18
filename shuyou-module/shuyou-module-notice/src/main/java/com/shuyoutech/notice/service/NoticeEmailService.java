package com.shuyoutech.notice.service;

import com.shuyoutech.notice.domain.bo.NoticeSendBo;

import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-19 14:00
 **/
public interface NoticeEmailService {

    /**
     * 处理通知消息
     *
     * @param bo 通知请求对象
     */
    void handler(NoticeSendBo bo);

    /**
     * 简单的邮件发送
     *
     * @param email   邮箱
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String email, String subject, String content);

    /**
     * @param email   邮箱
     * @param subject 主题
     * @param content 内容
     */
    void sendMimeMail(String email, String subject, String content);

    /**
     * 发送邮件附件
     *
     * @param email         邮箱
     * @param subject       主题
     * @param content       内容
     * @param attachmentMap 附件Map
     */
    void sendAttachmentMail(String email, String subject, String content, Map<String, String> attachmentMap);

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return boolean
     */
    Boolean validateEmailCode(String email, String code);

    /**
     * 清除邮箱验证码
     *
     * @param email 邮箱
     */
    void cleanEmailCode(String email);

}
