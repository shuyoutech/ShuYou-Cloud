package com.shuyoutech.pay.service;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.pay.domain.bo.*;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author YangChao
 * @date 2025-08-20 11:48
 **/
public interface PayService {

    JSONObject payPrepay(PayPrepayBo bo);

    JSONObject queryOrderByOutTradeNo(QueryOrderByOutTradeNoBo bo);

    JSONObject queryOrderById(QueryOrderByIdBo bo);

    void closeOrder(CloseOrderBo bo);

    void payNotify(String channelCode, HttpServletRequest request);

    JSONObject refund(PayRefundBo bo);

}
