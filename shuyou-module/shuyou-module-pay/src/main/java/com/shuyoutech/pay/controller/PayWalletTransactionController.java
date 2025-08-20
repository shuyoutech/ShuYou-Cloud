package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.pay.domain.bo.PayWalletTransactionBo;
import com.shuyoutech.pay.domain.vo.PayWalletTransactionVo;
import com.shuyoutech.pay.service.PayWalletTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payWalletTransaction")
@Tag(name = "PayWalletTransactionController", description = "会员钱包流水管理API控制器")
public class PayWalletTransactionController {

    @PostMapping("page")
    @Operation(description = "会员钱包流水分页列表")
    public R<PageResult<PayWalletTransactionVo>> page(@RequestBody PageQuery<PayWalletTransactionBo> pageQuery) {
        return R.success(payWalletTransactionService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询会员钱包流水详情")
    public R<PayWalletTransactionVo> detail(@PathVariable String id) {
        return R.success(payWalletTransactionService.detail(id));
    }

    private final PayWalletTransactionService payWalletTransactionService;

}
