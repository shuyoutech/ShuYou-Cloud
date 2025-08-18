package com.shuyoutech.notice.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.shuyoutech.api.enums.NoticeTypeEnum;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.core.util.TemplateUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.constant.CacheConstants;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.notice.config.NoticeProperties;
import com.shuyoutech.notice.domain.bo.NoticeSendBo;
import com.shuyoutech.notice.domain.entity.NoticeRecordEntity;
import com.shuyoutech.notice.domain.entity.SmsTemplateEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.common.redis.constant.CacheConstants.CAPTCHA_SMS_KEY;

/**
 * @author YangChao
 * @date 2025-07-19 14:00
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeSmsServiceImpl implements NoticeSmsService {

    private static Client client;

    @PostConstruct
    public void init() throws Exception {
        Config config = new Config()
                // 配置 AccessKey ID，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(noticeProperties.getAccessKey())
                // 配置 AccessKey Secret，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(noticeProperties.getSecretKey());

        // 配置 Endpoint
        config.endpoint = "dysmsapi.aliyuncs.com";

        // 初始化客户端 client
        client = new Client(config);
    }

    @Override
    public void handler(NoticeSendBo bo) {
        SmsTemplateEntity smsTemplate = smsTemplateService.getById(bo.getTemplateId());
        if (null == smsTemplate) {
            throw new BusinessException("未找到此SMS模板id的模板");
        }
        String code = RandomUtil.randomNumbers(4);
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        String content = TemplateUtils.format(smsTemplate.getTemplateParam(), params);

        NoticeRecordEntity record = new NoticeRecordEntity();
        record.setId(IdUtil.simpleUUID());
        record.setCreateTime(new Date());
        record.setTemplateId(bo.getTemplateId());
        record.setRecipient(bo.getSendAddr());
        record.setTitle(smsTemplate.getTemplateName());
        record.setType(NoticeTypeEnum.SMS.getValue());
        record.setContent(content);
        MongoUtils.save(record);

        // 构造请求对象，请填入请求参数值
        SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(bo.getSendAddr()).setSignName(smsTemplate.getSignName())
                // SMS_15305****
                .setTemplateCode(smsTemplate.getTemplateCode())
                // {\"name\":\"张三\",\"number\":\"1390000****\"}
                .setTemplateParam(content);
        this.sendSms(sendSmsRequest);
        RedisUtils.set(CAPTCHA_SMS_KEY + bo.getSendAddr(), code, 5L, TimeUnit.MINUTES);
    }

    @Override
    public void sendSms(String mobile, String code, String templateCode) {
        SmsTemplateEntity smsTemplate = smsTemplateService.getById(templateCode);
        if (null == smsTemplate) {
            throw new BusinessException(StringUtils.format("未找到此SMS模板id的模板:{}", templateCode));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        String content = TemplateUtils.format(smsTemplate.getTemplateParam(), params);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()  //
                .setPhoneNumbers(mobile) //
                .setSignName(smsTemplate.getSignName()) //
                .setTemplateCode(templateCode) //
                .setTemplateParam(content);
        sendSms(sendSmsRequest);

        RedisUtils.set(CacheConstants.CAPTCHA_SMS_KEY + mobile, code, 5L, TimeUnit.MINUTES);
    }

    @Override
    public void sendSms(SendSmsRequest request) {
        try {
            log.info("sendSms ========== request:{}", JSON.toJSONString(request));
            SendSmsResponse response = client.sendSms(request);
            log.info("sendSms ========== response:{}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.error("sendSms ========== exception:{}", e.getMessage());
        }
    }

    @Override
    public boolean validateSmsCode(String mobile, String code) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
            return false;
        }
        String captcha = RedisUtils.getString(CacheConstants.CAPTCHA_SMS_KEY + mobile);
        return code.equals(captcha);
    }

    @Override
    public void cleanSmsCode(String mobile) {
        RedisUtils.delete(CacheConstants.CAPTCHA_SMS_KEY + mobile);
    }

    private final NoticeProperties noticeProperties;
    private final SmsTemplateService smsTemplateService;

}
