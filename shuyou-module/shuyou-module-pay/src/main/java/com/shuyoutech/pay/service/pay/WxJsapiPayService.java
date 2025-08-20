package com.shuyoutech.pay.service.pay;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.pay.config.WxPayConfig;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;

/**
 * @author YangChao
 * @date 2025-08-20 10:34
 **/
public interface WxJsapiPayService {

    /**
     * JSAPI/小程序下单
     *
     * @param amount 金额 分
     * @return 预支付交易会话标识
     */
    JSONObject prepay(WxPayConfig wxPayConfig, Integer amount);

    /**
     * 商户订单号查询
     *
     * @param mchId      商户号
     * @param outTradeNo 商户订单号
     * @return 订单信息
     */
    Transaction queryOrderByOutTradeNo(String mchId, String outTradeNo);

    /**
     * 微信支付订单号查询
     *
     * @param mchId         商户号
     * @param transactionId 微信支付订单号
     * @return 订单信息
     */
    Transaction queryOrderById(String mchId, String transactionId);

    /**
     * 关闭订单API
     *
     * @param mchId      商户号
     * @param outTradeNo 商户订单号
     */
    void closeOrder(String mchId, String outTradeNo);

    /**
     * 退款申请
     *
     * @param wxPayConfig 配置
     * @param amount      退款金额
     * @param reason      退款原因
     * @param payOrder    订单信息
     * @return 退款信息
     */
    JSONObject refund(WxPayConfig wxPayConfig, Integer amount, String reason, PayOrderEntity payOrder);

    /**
     * 查询单笔退款
     *
     * @param outRefundNo 商户订单号
     * @return 订单信息
     */
    Refund queryRefund(String outRefundNo);
}
