package com.shuyoutech.pay.init;

import com.shuyoutech.pay.config.WxPayProperties;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RSAPublicKeyNotificationConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author YangChao
 * @date 2025-07-23 10:11
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class PayRunner implements CommandLineRunner {

    public static NativePayService nativePayService;
    public static NotificationParser notificationParser;
    public static AeadAesCipher aeadAesCipher;

    @Override
    public void run(String... args) {
        try {
            // 使用微信支付公钥的RSA配置 https://github.com/wechatpay-apiv3/wechatpay-java
            Config config = new RSAPublicKeyConfig.Builder() //
                    .merchantId(wxPayProperties.getMchId()) //微信支付的商户号
                    .privateKeyFromPath(wxPayProperties.getPrivateKeyFromPath()) // 商户API证书私钥的存放路径
                    .publicKeyFromPath(wxPayProperties.getPublicKeyFromPath()) //微信支付公钥的存放路径
                    .publicKeyId(wxPayProperties.getPublicKeyId()) //微信支付公钥ID
                    .merchantSerialNumber(wxPayProperties.getMerchantSerialNumber()) //商户API证书序列号
                    .apiV3Key(wxPayProperties.getApiV3Key()) //APIv3密钥
                    .build();
            // 构建Native service
            nativePayService = new NativePayService.Builder().config(config).build();

            // 1. 如果你使用的是微信支付公私钥，则使用 RSAPublicKeyNotificationConfig
            NotificationConfig notificationConfig = new RSAPublicKeyNotificationConfig.Builder()
                    .publicKeyFromPath(wxPayProperties.getPublicKeyFromPath())
                    .publicKeyId(wxPayProperties.getPublicKeyId())
                    .apiV3Key(wxPayProperties.getApiV3Key())
                    .build();
            notificationParser = new NotificationParser(notificationConfig);

            // 解密
            aeadAesCipher = new AeadAesCipher(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            log.error("PayRunner run ======================== exception:{}", exception.getMessage());
        }
    }

    private final WxPayProperties wxPayProperties;

}
