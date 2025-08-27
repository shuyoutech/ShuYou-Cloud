package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.pay.domain.bo.PayRechargePackageBo;
import com.shuyoutech.pay.domain.vo.PayRechargePackageVo;
import com.shuyoutech.pay.service.PayRechargePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payRechargePackage")
@Tag(name = "PayRechargePackageController", description = "充值套餐管理API控制器")
public class PayRechargePackageController {

    @PostMapping("page")
    @Operation(description = "充值套餐分页列表")
    public R<PageResult<PayRechargePackageVo>> page(@RequestBody PageQuery<PayRechargePackageBo> pageQuery) {
        return R.success(payRechargePackageService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询充值套餐详情")
    public R<PayRechargePackageVo> detail(@PathVariable String id) {
        return R.success(payRechargePackageService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增充值套餐")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody PayRechargePackageBo bo) {
        return R.success(payRechargePackageService.savePayRechargePackage(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改充值套餐")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody PayRechargePackageBo bo) {
        return R.success(payRechargePackageService.updatePayRechargePackage(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除充值套餐")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(payRechargePackageService.deletePayRechargePackage(ids));
    }

    private final PayRechargePackageService payRechargePackageService;

}
