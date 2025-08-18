package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.pay.domain.bo.PayNotifyRecordBo;
import com.shuyoutech.pay.domain.vo.PayNotifyRecordVo;
import com.shuyoutech.pay.service.PayNotifyRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author YangChao
 * @date 2025-07-23 16:26:15
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payNotifyRecord")
@Tag(name = "PayNotifyRecordController", description = "支付通知记录管理API控制器")
public class PayNotifyRecordController {

    @PostMapping("page")
    @Operation(description = "支付通知记录分页列表")
    public R<PageResult<PayNotifyRecordVo>> page(@RequestBody PageQuery<PayNotifyRecordBo> pageQuery) {
        return R.success(payNotifyRecordService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询支付通知记录详情")
    public R<PayNotifyRecordVo> detail(@PathVariable String id) {
        return R.success(payNotifyRecordService.detail(id));
    }

    private final PayNotifyRecordService payNotifyRecordService;

}
