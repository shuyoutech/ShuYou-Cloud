package com.shuyoutech.member.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.member.domain.bo.MemberWalletBo;
import com.shuyoutech.member.domain.vo.MemberWalletVo;
import com.shuyoutech.member.service.MemberWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("memberWallet")
@Tag(name = "MemberWalletController", description = "会员钱包管理API控制器")
public class MemberWalletController {

    @PostMapping("page")
    @Operation(description = "会员钱包分页列表")
    public R<PageResult<MemberWalletVo>> page(@RequestBody PageQuery<MemberWalletBo> pageQuery) {
        return R.success(memberWalletService.page(pageQuery));
    }

    @PostMapping(path = "get")
    @Operation(description = "获得用户钱包详情")
    public R<MemberWalletVo> getWallet() {
        return R.success(memberWalletService.detail(AuthUtils.getLoginUserId()));
    }

    private final MemberWalletService memberWalletService;

}
