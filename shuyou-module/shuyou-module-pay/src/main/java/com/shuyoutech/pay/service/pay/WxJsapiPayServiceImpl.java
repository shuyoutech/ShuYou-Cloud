package com.shuyoutech.pay.service.pay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.PayChannelEnum;
import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.api.enums.PayTradeTypeEnum;
import com.shuyoutech.common.core.constant.DateConstants;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.pay.config.WxPayConfig;
import com.shuyoutech.pay.domain.entity.PayChannelEntity;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RSAPublicKeyNotificationConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791894">小程序支付，提供商户在自身微信小程序中使用微信支付收款的能力。</a>
 *
 * @author YangChao
 * @date 2025-08-20 10:36
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class WxJsapiPayServiceImpl implements WxJsapiPayService {

    public static JsapiServiceExtension jsapiService;
    public static NotificationParser notificationParser;

    @PostConstruct
    public void init() {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("channelCode").is(PayChannelEnum.WEIXIN_MP.getValue()));
            PayChannelEntity payChannel = MongoUtils.selectOne(query, PayChannelEntity.class);
            if (null == payChannel) {
                return;
            }
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);

            // 使用微信支付公钥的RSA配置 https://github.com/wechatpay-apiv3/wechatpay-java
            Config config = new RSAPublicKeyConfig.Builder() //
                    .merchantId(wxPayConfig.getMchId()) //微信支付的商户号
                    .privateKeyFromPath(wxPayConfig.getPrivateKeyFromPath()) // 商户API证书私钥的存放路径
                    .publicKeyFromPath(wxPayConfig.getPublicKeyFromPath()) //微信支付公钥的存放路径
                    .publicKeyId(wxPayConfig.getPublicKeyId()) //微信支付公钥ID
                    .merchantSerialNumber(wxPayConfig.getMerchantSerialNumber()) //商户API证书序列号
                    .apiV3Key(wxPayConfig.getApiV3Key()) //APIv3密钥
                    .build();

            // 构建小程序 service
            jsapiService = new JsapiServiceExtension.Builder().config(config).build();

            // 1. 如果你使用的是微信支付公私钥，则使用 RSAPublicKeyNotificationConfig
            NotificationConfig notificationConfig = new RSAPublicKeyNotificationConfig.Builder() //
                    .publicKeyFromPath(wxPayConfig.getPublicKeyFromPath()) //
                    .publicKeyId(wxPayConfig.getPublicKeyId()) //
                    .apiV3Key(wxPayConfig.getApiV3Key()) //
                    .build();

            notificationParser = new NotificationParser(notificationConfig);
        } catch (Exception exception) {
            log.error("init ======================== exception:{}", exception.getMessage());
        }
    }

    @Override
    public JSONObject prepay(WxPayConfig wxPayConfig, Integer amount) {
        // 订单号
        Date now = new Date();
        String orderId = DateUtil.format(now, DateConstants.PURE_DATETIME_FORMAT) + IdUtil.getSnowflakeNextIdStr();
        String appid = wxPayConfig.getAppid();
        String mchId = wxPayConfig.getMchId();

        // 订单记录插入
        PayOrderEntity payOrder = new PayOrderEntity();
        payOrder.setId(orderId);
        payOrder.setCreateTime(now);
        payOrder.setStatus(PayOrderStatusEnum.GENERATE.getValue());
        payOrder.setTradeType(PayTradeTypeEnum.JSAPI.getValue());
        payOrder.setCreateUserId(AuthUtils.getLoginUserId());
        payOrder.setCreateUserName(AuthUtils.getLoginUserName());
        payOrder.setAppId(appid);
        payOrder.setMchId(mchId);
        payOrder.setAmount(amount);
        payOrder.setExpiredTime(DateUtil.offsetHour(now, 2));
        MongoUtils.save(payOrder);

        // 支付下单
        PrepayRequest request = new PrepayRequest();
        Amount amt = new Amount();
        amt.setTotal(amount);
        amt.setCurrency("CNY");
        request.setAmount(amt);
        request.setAppid(appid);
        request.setMchid(mchId);
        request.setDescription("数游AI-JSAPI");
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(orderId);
        request.setTimeExpire(DateUtil.format(payOrder.getExpiredTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        Payer payer = new Payer();
        payer.setOpenid(AuthUtils.getLoginUserOpenid());
        request.setPayer(payer);
        PrepayWithRequestPaymentResponse response = jsapiService.prepayWithRequestPayment(request);

        // 更新支付订单表状态为支付中
        Update update = new Update();
        update.set("status", PayOrderStatusEnum.PAYING.getValue());
        MongoUtils.patch(orderId, update, PayOrderEntity.class);

        JSONObject vo = new JSONObject();
        vo.put("outTradeNo", orderId);
        vo.put("appId", response.getAppId());
        vo.put("timestamp", response.getTimeStamp());
        vo.put("nonceStr", response.getNonceStr());
        vo.put("packageVal", response.getPackageVal());
        vo.put("signType", response.getSignType());
        vo.put("paySign", response.getPaySign());
        return vo;
    }

    @Override
    public Transaction queryOrderByOutTradeNo(String mchId, String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(mchId);
        request.setOutTradeNo(outTradeNo);
        return jsapiService.queryOrderByOutTradeNo(request);
    }

    @Override
    public Transaction queryOrderById(String mchId, String transactionId) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setMchid(mchId);
        request.setTransactionId(transactionId);
        return jsapiService.queryOrderById(request);
    }

    @Override
    public void closeOrder(String mchId, String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(mchId);
        request.setOutTradeNo(outTradeNo);
        jsapiService.closeOrder(request);

        Update update = new Update();
        update.set("status", PayOrderStatusEnum.CLOSE.getValue());
        MongoUtils.patch(outTradeNo, update, PayOrderEntity.class);
    }

}
