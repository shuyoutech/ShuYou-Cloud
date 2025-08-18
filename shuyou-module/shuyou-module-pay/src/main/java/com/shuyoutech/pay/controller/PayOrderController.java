package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.pay.domain.bo.PayOrderBo;
import com.shuyoutech.pay.domain.vo.PayOrderVo;
import com.shuyoutech.pay.service.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-23 11:04:39
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payOrder")
@Tag(name = "PayOrderController", description = "支付订单管理API控制器")
public class PayOrderController {

    @PostMapping("page")
    @Operation(description = "支付订单分页列表")
    public R<PageResult<PayOrderVo>> page(@RequestBody PageQuery<PayOrderBo> pageQuery) {
        return R.success(payOrderService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询支付订单详情")
    public R<PayOrderVo> detail(@PathVariable String id) {
        return R.success(payOrderService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增支付订单")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody PayOrderBo bo) {
        return R.success(payOrderService.savePayOrder(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改支付订单")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody PayOrderBo bo) {
        return R.success(payOrderService.updatePayOrder(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除支付订单")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(payOrderService.deletePayOrder(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(payOrderService.checkUnique(param));
    }

    @PostMapping(path = "status")
    @Operation(description = "状态修改")
    public R<Boolean> status(@Validated({StatusGroup.class}) @RequestBody PayOrderBo bo) {
        return R.success(payOrderService.statusPayOrder(bo.getId(), bo.getStatus()));
    }

    private final PayOrderService payOrderService;

}
