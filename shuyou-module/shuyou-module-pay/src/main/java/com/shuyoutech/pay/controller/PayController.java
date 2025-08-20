package com.shuyoutech.pay.controller;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.pay.domain.bo.*;
import com.shuyoutech.pay.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791874">微信支付文档</a>
 *
 * @author YangChao
 * @date 2025-07-23 09:44
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "PayController", description = "支付API控制器")
public class PayController {

    @PostMapping("/pay/prepay")
    @Operation(summary = "支付下单")
    public R<JSONObject> payPrepay(@Validated @RequestBody PayPrepayBo bo) {
        return R.success(payService.payPrepay(bo));
    }

    @PostMapping("/pay/queryOrderByOutTradeNo")
    @Operation(summary = "商户订单号查询订单")
    public R<JSONObject> queryOrderByOutTradeNo(@Validated @RequestBody QueryOrderByOutTradeNoBo bo) {
        return R.success(payService.queryOrderByOutTradeNo(bo));
    }

    @PostMapping("/pay/queryOrderById")
    @Operation(summary = "支付订单号查询订单")
    public R<JSONObject> queryOrderById(@Validated @RequestBody QueryOrderByIdBo bo) {
        return R.success(payService.queryOrderById(bo));
    }

    @PostMapping("/pay/closeOrder")
    @Operation(summary = "关闭订单")
    public R<Void> closeOrder(@Validated @RequestBody CloseOrderBo bo) {
        payService.closeOrder(bo);
        return R.success();
    }

    @PostMapping("/pay/notify/{channelCode}")
    @Operation(summary = "支付渠道成功回调通知")
    public R<Void> payNotify(@PathVariable("channelCode") String channelCode, HttpServletRequest request) {
        payService.payNotify(channelCode, request);
        return R.success();
    }

    @PostMapping("/pay/refund")
    @Operation(summary = "申请退款")
    public R<JSONObject> refund(@Validated @RequestBody PayRefundBo bo) {
        return R.success(payService.refund(bo));
    }

    @PostMapping("/pay/queryRefund")
    @Operation(summary = "查询单笔退款")
    public R<JSONObject> queryRefund(@Validated @RequestBody QueryRefundBo bo) {
        return R.success(payService.queryRefund(bo));
    }

    @PostMapping("/refund/notify/{channelCode}")
    @Operation(summary = "支付渠道成功回调通知")
    public R<Void> refundNotify(@PathVariable("channelCode") String channelCode, HttpServletRequest request) {
        payService.payNotify(channelCode, request);
        return R.success();
    }

    private final PayService payService;

}
