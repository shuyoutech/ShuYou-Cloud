package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.pay.domain.bo.PayRefundBo;
import com.shuyoutech.pay.domain.vo.PayRefundVo;
import com.shuyoutech.pay.service.PayRefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author YangChao
 * @date 2025-08-20 19:57:14
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payRefund")
@Tag(name = "PayRefundController", description = "支付退款管理API控制器")
public class PayRefundController {

    @PostMapping("page")
    @Operation(description = "支付退款分页列表")
    public R<PageResult<PayRefundVo>> page(@RequestBody PageQuery<PayRefundBo> pageQuery) {
        return R.success(payRefundService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询支付退款详情")
    public R<PayRefundVo> detail(@PathVariable String id) {
        return R.success(payRefundService.detail(id));
    }

    private final PayRefundService payRefundService;

}
