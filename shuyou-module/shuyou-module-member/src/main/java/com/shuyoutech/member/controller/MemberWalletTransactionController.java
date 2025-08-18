package com.shuyoutech.member.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.member.domain.bo.MemberWalletTransactionBo;
import com.shuyoutech.member.domain.vo.MemberWalletTransactionVo;
import com.shuyoutech.member.service.MemberWalletTransactionService;
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
@RequestMapping("memberWalletTransaction")
@Tag(name = "MemberWalletTransactionController", description = "会员钱包流水管理API控制器")
public class MemberWalletTransactionController {

    @PostMapping("page")
    @Operation(description = "会员钱包流水分页列表")
    public R<PageResult<MemberWalletTransactionVo>> page(@RequestBody PageQuery<MemberWalletTransactionBo> pageQuery) {
        return R.success(memberWalletTransactionService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询会员钱包流水详情")
    public R<MemberWalletTransactionVo> detail(@PathVariable String id) {
        return R.success(memberWalletTransactionService.detail(id));
    }

    private final MemberWalletTransactionService memberWalletTransactionService;

}
