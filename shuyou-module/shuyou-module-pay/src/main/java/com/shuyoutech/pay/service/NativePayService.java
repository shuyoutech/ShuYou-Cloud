package com.shuyoutech.pay.service;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.pay.domain.vo.PayNativePrepayVo;
import com.wechat.pay.java.service.payments.model.Transaction;

/**
 * @author YangChao
 * @date 2025-07-23 10:01
 **/
public interface NativePayService {

    /**
     * 下单
     *
     * @param amount - 金额 分
     * @return 二维码地址
     */
    PayNativePrepayVo prepay(Integer amount);

    /**
     * 订单支付成功后，商户可使用微信订单号查询订单或商户订单号查询订单；若订单未支付，则只能使用商户订单号查询订单。
     *
     * @param outTradeNo 商户下单时传入的商户系统内部订单号。
     * @return 订单信息
     */
    Transaction queryOrderByOutTradeNo(String outTradeNo);

    /**
     * 订单支付成功后，商户可通过微信交易订单号或使用商户订单号查询订单；若订单未支付，则只能使用商户订单号查询订单。
     *
     * @param transactionId 微信支付侧订单的唯一标识，订单支付成功后，支付成功回调通知和商户订单号查询订单会返回该参数。
     * @return 订单信息
     */
    Transaction queryOrderById(String transactionId);

    /**
     * 关闭订单API
     *
     * @param outTradeNo 商户下单时传入的商户系统内部订单号。
     */
    void closeOrder(String outTradeNo);

    /**
     * 回调
     *
     * @param jsonObject - 请求json
     */
    void notify(JSONObject jsonObject);

}
