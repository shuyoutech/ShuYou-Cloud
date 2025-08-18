package com.shuyoutech.pay.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.api.enums.PayTradeTypeEnum;
import com.shuyoutech.common.core.constant.DateConstants;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.pay.config.WxPayProperties;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.shuyoutech.pay.domain.vo.PayNativePrepayVo;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static com.shuyoutech.pay.init.PayRunner.aeadAesCipher;
import static com.shuyoutech.pay.init.PayRunner.nativePayService;

/**
 * @author YangChao
 * @date 2025-07-23 10:02
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NativePayServiceImpl implements NativePayService {

    @Override
    public PayNativePrepayVo prepay(Integer amount) {
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
        payOrder.setAppId(wxPayProperties.getAppid());
        payOrder.setMchId(wxPayProperties.getMchId());
        payOrder.setAmount(amount);
        payOrder.setExpiredTime(DateUtil.offsetHour(now, 2));
        MongoUtils.save(payOrder);

        // 请求微信支付获取二维码地址
        PrepayRequest request = new PrepayRequest();
        Amount amt = new Amount();
        amt.setCurrency("CNY");
        amt.setTotal(amount);
        request.setAmount(amt);
        request.setAppid(wxPayProperties.getAppid());
        request.setMchid(wxPayProperties.getMchId());
        request.setDescription("AI Token NATIVE");
        request.setNotifyUrl(wxPayProperties.getNotifyUrl());
        request.setOutTradeNo(orderId);
        request.setTimeExpire(DateUtil.format(payOrder.getExpiredTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        // 调用下单方法，得到应答
        PrepayResponse response = nativePayService.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        String codeUrl = response.getCodeUrl();
        if (StringUtils.isEmpty(codeUrl)) {
            log.error("prepay ===================== 调用下单接口失败!");
            return null;
        }
        // 更新支付订单表状态为支付中
        Update update = new Update();
        update.set("status", PayOrderStatusEnum.PAYING.getValue());
        MongoUtils.patch(orderId, update, PayOrderEntity.class);

        PayNativePrepayVo vo = new PayNativePrepayVo();
        vo.setOutTradeNo(orderId);
        vo.setCodeUrl(codeUrl);
        return vo;
    }

    @Override
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wxPayProperties.getMchId());
        request.setOutTradeNo(outTradeNo);
        return nativePayService.queryOrderByOutTradeNo(request);
    }

    @Override
    public Transaction queryOrderById(String transactionId) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setMchid(wxPayProperties.getMchId());
        request.setTransactionId(transactionId);
        return nativePayService.queryOrderById(request);
    }

    @Override
    public void closeOrder(String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(wxPayProperties.getMchId());
        request.setOutTradeNo(outTradeNo);
        nativePayService.closeOrder(request);
    }

    @Async
    @Override
    public void notify(JSONObject jsonObject) {
        String json = jsonObject.toString();
        String recordId = IdUtil.fastSimpleUUID();
        Date now = new Date();

        // 接收到之后插入商户通知记录表中
        PayNotifyRecordEntity record = new PayNotifyRecordEntity();
        record.setId(recordId);
        record.setCreateTime(now);
        record.setRequestBody(json);
        MongoUtils.save(record);

        dealNotifyJson(recordId, json);
    }

    private void dealNotifyJson(String recordId, String json) {
        if (StringUtil.isEmpty(json)) {
            return;
        }
        // 解析微信推送通知信息
        String associatedData = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.associated_data");
        String ciphertext = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.ciphertext");
        String nonce = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.nonce");
        String decrypt = aeadAesCipher.decrypt(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(ciphertext));
        String mchId = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "mchid");
        String appId = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "appid");
        String tradeState = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "trade_state");
        String outTradeNo = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "out_trade_no");
        String transactionId = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "transaction_id");
        String successTime = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "success_time");
        Integer amount = (Integer) JSONUtil.getByPath(JSONUtil.parse(decrypt), "amount.total");
        String tradeType = (String) JSONUtil.getByPath(JSONUtil.parse(decrypt), "trade_type");

        PayOrderEntity payOrder = payOrderService.getById(outTradeNo);
        if (null == payOrder) {
            log.error("dealNotifyJson ============================= 订单号：{} 不存在!", outTradeNo);
            return;
        }
        Update update = new Update();
        if ("SUCCESS".equalsIgnoreCase(tradeState)) {
            update.set("status", PayOrderStatusEnum.SUCCESS.getValue());
            update.set("successTime", DateUtil.parse(successTime, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN).toJdkDate());
        } else {
            update.set("status", PayOrderStatusEnum.FAILED.getValue());
        }
        update.set("mchOrderNo", transactionId);
        MongoUtils.patch(outTradeNo, update, PayOrderEntity.class);

        Update update2 = new Update();
        update2.set("status", "2");
        update2.set("tradeType", tradeType);
        update2.set("orderId", outTradeNo);
        update2.set("mchId", mchId);
        update2.set("appId", appId);
        update2.set("mchOrderNo", transactionId);
        update2.set("amount", amount);
        MongoUtils.patch(recordId, update2, PayNotifyRecordEntity.class);
    }

    private final WxPayProperties wxPayProperties;
    private final PayOrderService payOrderService;
}
