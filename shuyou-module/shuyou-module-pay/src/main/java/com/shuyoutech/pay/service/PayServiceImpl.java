package com.shuyoutech.pay.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.PayChannelEnum;
import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import com.shuyoutech.pay.config.WxPayConfig;
import com.shuyoutech.pay.domain.bo.*;
import com.shuyoutech.pay.domain.entity.PayChannelEntity;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.shuyoutech.pay.service.pay.WxJsapiPayService;
import com.shuyoutech.pay.service.pay.WxNativePayService;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static com.shuyoutech.pay.service.pay.WxJsapiPayServiceImpl.notificationParser;

/**
 * @author YangChao
 * @date 2025-08-20 11:48
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    @Override
    public JSONObject payPrepay(PayPrepayBo bo) {
        String channelCode = bo.getChannelCode();
        Integer amount = bo.getAmount();
        Query query = new Query();
        query.addCriteria(Criteria.where("channelCode").is(channelCode));
        PayChannelEntity payChannel = payChannelService.selectOne(query);
        if (null == payChannel) {
            log.error("payPrepay =============== channelCode:{} is not exist", channelCode);
            throw new BusinessException("channelCode is not exist");
        }
        if (PayChannelEnum.WEIXIN_NATIVE.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            return wxNativePayService.prepay(wxPayConfig, amount);
        } else if (PayChannelEnum.WEIXIN_MP.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            return wxJsapiPayService.prepay(wxPayConfig, amount);
        }
        return null;
    }

    @Override
    public JSONObject queryOrderByOutTradeNo(QueryOrderByOutTradeNoBo bo) {
        String channelCode = bo.getChannelCode();
        String outTradeNo = bo.getOutTradeNo();
        Query query = new Query();
        query.addCriteria(Criteria.where("channelCode").is(channelCode));
        PayChannelEntity payChannel = payChannelService.selectOne(query);
        if (null == payChannel) {
            log.error("queryOrderByOutTradeNo =============== channelCode:{} is not exist", channelCode);
            throw new BusinessException("channelCode is not exist");
        }
        if (PayChannelEnum.WEIXIN_NATIVE.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            Transaction transaction = wxNativePayService.queryOrderByOutTradeNo(wxPayConfig.getMchId(), outTradeNo);
            return JSONObject.from(transaction);
        } else if (PayChannelEnum.WEIXIN_MP.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            Transaction transaction = wxJsapiPayService.queryOrderByOutTradeNo(wxPayConfig.getMchId(), outTradeNo);
            return JSONObject.from(transaction);
        }
        return null;
    }

    @Override
    public JSONObject queryOrderById(QueryOrderByIdBo bo) {
        String channelCode = bo.getChannelCode();
        String transactionId = bo.getTransactionId();
        Query query = new Query();
        query.addCriteria(Criteria.where("channelCode").is(channelCode));
        PayChannelEntity payChannel = payChannelService.selectOne(query);
        if (null == payChannel) {
            log.error("queryOrderById =============== channelCode:{} is not exist", channelCode);
            throw new BusinessException("channelCode is not exist");
        }
        if (PayChannelEnum.WEIXIN_NATIVE.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            Transaction transaction = wxNativePayService.queryOrderById(wxPayConfig.getMchId(), transactionId);
            return JSONObject.from(transaction);
        } else if (PayChannelEnum.WEIXIN_MP.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            Transaction transaction = wxJsapiPayService.queryOrderById(wxPayConfig.getMchId(), transactionId);
            return JSONObject.from(transaction);
        }
        return null;
    }

    @Override
    public void closeOrder(CloseOrderBo bo) {
        String channelCode = bo.getChannelCode();
        String outTradeNo = bo.getOutTradeNo();
        Query query = new Query();
        query.addCriteria(Criteria.where("channelCode").is(channelCode));
        PayChannelEntity payChannel = payChannelService.selectOne(query);
        if (null == payChannel) {
            log.error("closeOrder =============== channelCode:{} is not exist", channelCode);
            throw new BusinessException("channelCode is not exist");
        }
        if (PayChannelEnum.WEIXIN_NATIVE.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            wxNativePayService.closeOrder(wxPayConfig.getMchId(), outTradeNo);
        } else if (PayChannelEnum.WEIXIN_MP.getValue().equalsIgnoreCase(channelCode)) {
            WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
            wxJsapiPayService.closeOrder(wxPayConfig.getMchId(), outTradeNo);
        }
    }

    @Async
    @Override
    public void payNotify(String channelCode, HttpServletRequest request) {
        try {
            // 接收到之后插入商户通知记录表中
            String recordId = IdUtil.fastSimpleUUID();
            Date now = new Date();
            Map<String, String> headerMap = JakartaServletUtils.getHeaderMap(request);
            String body = JakartaServletUtils.getBody(request);

            PayNotifyRecordEntity record = new PayNotifyRecordEntity();
            record.setId(recordId);
            record.setCreateTime(now);
            record.setChannelCode(channelCode);
            record.setRequestHeaders(JSON.toJSONString(headerMap));
            record.setRequestParams(JSON.toJSONString(JakartaServletUtils.getParamMap(request)));
            record.setRequestBody(body);
            MongoUtils.save(record);

            Query query = new Query();
            query.addCriteria(Criteria.where("channelCode").is(channelCode));
            PayChannelEntity payChannel = payChannelService.selectOne(query);
            if (null == payChannel) {
                log.error("payNotify =============== channelCode:{} is not exist", channelCode);
                return;
            }

            if (PayChannelEnum.WEIXIN_NATIVE.getValue().equalsIgnoreCase(channelCode) //
                    || PayChannelEnum.WEIXIN_MP.getValue().equalsIgnoreCase(channelCode)) {
                // 构造 RequestParam
                RequestParam requestParam = new RequestParam.Builder() //
                        .serialNumber(headerMap.get("wechatpay-serial")) //
                        .nonce(headerMap.get("wechatpay-nonce")) //
                        .signature(headerMap.get("wechatpay-signature")) //
                        .timestamp(headerMap.get("wechatpay-timestamp")) //
                        .body(body) //
                        .build();
                // 以支付通知回调为例，验签、解密并转换成 Transaction
                Transaction transaction = notificationParser.parse(requestParam, Transaction.class);
                if (null == transaction) {
                    log.error("payNotify ======================= transaction is null");
                    return;
                }
                String outTradeNo = transaction.getOutTradeNo();
                PayOrderEntity payOrder = payOrderService.getById(outTradeNo);
                if (null == payOrder) {
                    log.error("payNotify ======================= outTradeNo：{} 不存在!", outTradeNo);
                    return;
                }
                Update update = new Update();
                if (Transaction.TradeStateEnum.SUCCESS == transaction.getTradeState()) {
                    update.set("status", PayOrderStatusEnum.SUCCESS.getValue());
                    update.set("successTime", DateUtil.parse(transaction.getSuccessTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN).toJdkDate());
                } else {
                    update.set("status", PayOrderStatusEnum.FAILED.getValue());
                }
                update.set("transactionId", transaction.getTransactionId());
                MongoUtils.patch(outTradeNo, update, PayOrderEntity.class);

                Update update2 = new Update();
                update2.set("tradeType", transaction.getTradeType());
                update2.set("tradeState", transaction.getTradeState());
                update2.set("outTradeNo", outTradeNo);
                update2.set("mchId", transaction.getMchid());
                update2.set("appId", transaction.getAppid());
                update2.set("transactionId", transaction.getTransactionId());
                update2.set("amount", transaction.getAmount().getTotal());
                MongoUtils.patch(record.getId(), update2, PayNotifyRecordEntity.class);

                String userId = payOrder.getCreateUserId();
                payWalletService.addWalletBalance(userId, WalletPayTypeEnum.RECHARGE, outTradeNo, transaction.getAmount().getTotal());
            }
        } catch (Exception e) {
            log.error("payNotify ================== exception:{}", e.getMessage());
        }

    }

    @Override
    public JSONObject refund(PayRefundBo bo) {
        PayOrderEntity payOrder = payOrderService.getById(bo.getOutTradeNo());
        if (null == payOrder) {
            throw new BusinessException("商户订单号不存在!");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("channelCode").is(PayChannelEnum.WEIXIN_MP.getValue()));
        PayChannelEntity payChannel = payChannelService.selectOne(query);
        if (null == payChannel) {
            throw new BusinessException("没有配置渠道!");
        }
        WxPayConfig wxPayConfig = JSONObject.parseObject(payChannel.getChannelConfig(), WxPayConfig.class);
        return wxJsapiPayService.refund(wxPayConfig, bo.getAmount(), bo.getReason(), payOrder);
    }

    private final PayChannelService payChannelService;
    private final WxNativePayService wxNativePayService;
    private final WxJsapiPayService wxJsapiPayService;
    private final PayOrderService payOrderService;
    private final PayWalletService payWalletService;

}
