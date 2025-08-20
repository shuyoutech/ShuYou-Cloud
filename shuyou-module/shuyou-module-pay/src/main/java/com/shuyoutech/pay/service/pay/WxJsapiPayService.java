package com.shuyoutech.pay.service.pay;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.pay.config.WxPayConfig;
import com.wechat.pay.java.service.payments.model.Transaction;

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

}
