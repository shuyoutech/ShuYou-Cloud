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
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791874">微信Native支付，提供商户在PC端网页浏览器中使用微信支付收款的能力。</a>
 *
 * @author YangChao
 * @date 2025-07-23 10:02
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class WxNativePayServiceImpl implements WxNativePayService {

    public static NativePayService nativePayService;

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

            // 构建Native service
            nativePayService = new NativePayService.Builder().config(config).build();
        } catch (Exception exception) {
            log.error("init ======================== exception:{}", exception.getMessage());
        }
    }

    @Override
    public JSONObject prepay(WxPayConfig wxPayConfig, Integer amount) {
        // 订单号
        Date now = new Date();
        String orderId = DateUtil.format(now, DateConstants.PURE_DATETIME_FORMAT) + IdUtil.getSnowflakeNextIdStr();

        // 订单记录插入
        PayOrderEntity payOrder = new PayOrderEntity();
        payOrder.setId(orderId);
        payOrder.setStatus(PayOrderStatusEnum.GENERATE.getValue());
        payOrder.setTradeType(PayTradeTypeEnum.NATIVE.getValue());
        payOrder.setCreateTime(now);
        payOrder.setCreateUserId(AuthUtils.getLoginUserId());
        payOrder.setCreateUserName(AuthUtils.getLoginUserName());
        payOrder.setAppId(wxPayConfig.getAppid());
        payOrder.setMchId(wxPayConfig.getMchId());
        payOrder.setAmount(amount);
        payOrder.setExpiredTime(DateUtil.offsetHour(now, 2));
        MongoUtils.save(payOrder);

        // 请求微信支付获取二维码地址
        PrepayRequest request = new PrepayRequest();
        Amount amt = new Amount();
        amt.setCurrency("CNY");
        amt.setTotal(amount);
        request.setAmount(amt);
        request.setAppid(wxPayConfig.getAppid());
        request.setMchid(wxPayConfig.getMchId());
        request.setDescription("AI Token NATIVE");
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(orderId);
        request.setTimeExpire(DateUtil.format(payOrder.getExpiredTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        // 调用下单方法，得到应答
        PrepayResponse response = nativePayService.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        String codeUrl = response.getCodeUrl();

        // 更新支付订单表状态为支付中
        Update update = new Update();
        update.set("status", PayOrderStatusEnum.PAYING.getValue());
        MongoUtils.patch(orderId, update, PayOrderEntity.class);

        JSONObject vo = new JSONObject();
        vo.put("codeUrl", codeUrl);
        vo.put("outTradeNo", orderId);
        return vo;
    }

    @Override
    public Transaction queryOrderByOutTradeNo(String mchId, String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(mchId);
        request.setOutTradeNo(outTradeNo);
        return nativePayService.queryOrderByOutTradeNo(request);
    }

    @Override
    public Transaction queryOrderById(String mchId, String transactionId) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setMchid(mchId);
        request.setTransactionId(transactionId);
        return nativePayService.queryOrderById(request);
    }

    @Override
    public void closeOrder(String mchId, String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(mchId);
        request.setOutTradeNo(outTradeNo);
        nativePayService.closeOrder(request);

        Update update = new Update();
        update.set("status", PayOrderStatusEnum.CLOSE.getValue());
        MongoUtils.patch(outTradeNo, update, PayOrderEntity.class);
    }
}
