package com.shuyoutech.pay.controller;

import com.alibaba.fastjson2.JSON;
import com.shuyoutech.common.core.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author YangChao
 * @date 2025-08-04 14:32
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/pay/notify")
@RequiredArgsConstructor
@Tag(name = "PayNotifyController", description = "支付回调通知API控制器")
public class PayNotifyController {

    @PostMapping("/order/{channelCode}")
    @Operation(summary = "支付渠道成功回调通知")
    public R<Object> notifyOrder(@PathVariable("channelCode") String channelCode, //
                                 @RequestParam(required = false) Map<String, String> params, //
                                 @RequestBody(required = false) String body, //
                                 @RequestHeader Map<String, String> headers) {
        log.info("[notifyOrder][channelCode({}) 回调数据({}/{})]", channelCode, JSON.toJSONString(params), body);
        return R.success();
    }


}
