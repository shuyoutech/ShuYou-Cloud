package com.shuyoutech.notice.service;

import cn.hutool.core.util.IdUtil;
import com.shuyoutech.api.enums.NoticeTypeEnum;
import com.shuyoutech.common.core.util.MapUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.core.util.TemplateUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.constant.CacheConstants;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.notice.domain.bo.NoticeSendBo;
import com.shuyoutech.notice.domain.entity.EmailTemplateEntity;
import com.shuyoutech.notice.domain.entity.NoticeRecordEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-19 14:00
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeEmailServiceImpl implements NoticeEmailService {

    @Value("${spring.mail.username}")
    public String fromEmail;

    @Override
    public void handler(NoticeSendBo bo) {
        EmailTemplateEntity emailTemplate = emailTemplateService.getById(bo.getTemplateId());
        if (null == emailTemplate) {
            log.error("handler ========== 未找到此EMAIL模板id的模板");
            return;
        }
        String content = TemplateUtils.format(bo.getContent(), bo.getParams());
        NoticeRecordEntity record = new NoticeRecordEntity();
        record.setId(IdUtil.simpleUUID());
        record.setCreateTime(new Date());
        record.setTemplateId(bo.getTemplateId());
        record.setRecipient(bo.getSendAddr());
        record.setContent(content);
        record.setTitle(emailTemplate.getSubject());
        record.setType(NoticeTypeEnum.EMAIL.getValue());
        MongoUtils.save(record);

        sendSimpleMail(bo.getSendAddr(), emailTemplate.getSubject(), content);
    }

    @Override
    public void sendSimpleMail(String email, String subject, String content) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(content);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.error("sendSimpleMail ========== exception:{}", e.getMessage());
        }
    }

    @Override
    public void sendMimeMail(String email, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(content);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("sendMimeMail ========== exception:{}", e.getMessage());
        }
    }

    @Override
    public void sendAttachmentMail(String email, String subject, String content, Map<String, String> attachmentMap) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(content);
            if (MapUtils.isNotEmpty(attachmentMap)) {
                for (String key : attachmentMap.keySet()) {
                    try {
                        File file = new File(attachmentMap.get(key));
                        if (file.exists()) {
                            messageHelper.addAttachment(key, new FileSystemResource(file));
                        }
                    } catch (MessagingException ignored) {

                    }
                }
            }
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("sendAttachmentMail ========== exception:{}", e.getMessage());
        }
    }

    @Override
    public Boolean validateEmailCode(String email, String code) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            return false;
        }
        String captcha = RedisUtils.getString(CacheConstants.CAPTCHA_EMAIL_KEY + email);
        return code.equals(captcha);
    }

    @Override
    public void cleanEmailCode(String email) {
        RedisUtils.delete(CacheConstants.CAPTCHA_EMAIL_KEY + email);
    }

    private final EmailTemplateService emailTemplateService;
    private final JavaMailSender javaMailSender;
}
