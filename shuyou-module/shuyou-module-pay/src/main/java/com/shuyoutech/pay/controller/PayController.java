package com.shuyoutech.pay.controller;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.pay.domain.bo.PayNativePrepayBo;
import com.shuyoutech.pay.domain.vo.PayNativePrepayVo;
import com.shuyoutech.pay.service.NativePayService;
import com.wechat.pay.java.service.payments.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791874">...</a>
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

    @PostMapping("/wxpay/native/prepay")
    @Operation(summary = "Native下单", description = "商户通过调用Native支付下单API接口生成订单并获取二维码code_url，随后将该code_url传递至前端，由前端将其转换为二维码图片展示给用户。")
    public R<PayNativePrepayVo> nativePrepay(@Validated @RequestBody PayNativePrepayBo bo) {
        return R.success(nativePayService.prepay(bo.getAmount()));
    }

    @PostMapping("/wxpay/native/queryOrderByOutTradeNo")
    @Operation(summary = "Native商户订单号查询订单")
    public R<Transaction> queryOrderByOutTradeNo(@RequestBody JSONObject data) {
        Transaction transaction = nativePayService.queryOrderByOutTradeNo(data.getString("outTradeNo"));
        return R.success(transaction);
    }

    @PostMapping("/wxpay/native/queryOrderById")
    @Operation(summary = "Native微信支付订单号查询订单")
    public R<Transaction> queryOrderById(@RequestBody JSONObject data) {
        Transaction transaction = nativePayService.queryOrderById(data.getString("transactionId"));
        return R.success(transaction);
    }

    @PostMapping("/wxpay/native/closeOrder")
    @Operation(summary = "Native关闭订单")
    public R<Void> closeOrder(@RequestBody JSONObject data) {
        nativePayService.closeOrder(data.getString("outTradeNo"));
        return R.success();
    }

    @PostMapping("/wxpay/notify")
    @Operation(summary = "Native支付成功回调通知")
    public R<Object> notify(@RequestBody JSONObject jsonObject) {
        nativePayService.notify(jsonObject);
        return R.success();
    }

    private final NativePayService nativePayService;

}
