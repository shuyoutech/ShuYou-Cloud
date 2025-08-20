package com.shuyoutech.pay.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.pay.domain.bo.PayChannelBo;
import com.shuyoutech.pay.domain.vo.PayChannelVo;
import com.shuyoutech.pay.service.PayChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payChannel")
@Tag(name = "PayChannelController", description = "支付渠道管理API控制器")
public class PayChannelController {

    @PostMapping("page")
    @Operation(description = "支付渠道分页列表")
    public R<PageResult<PayChannelVo>> page(@RequestBody PageQuery<PayChannelBo> pageQuery) {
        return R.success(payChannelService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询支付渠道详情")
    public R<PayChannelVo> detail(@PathVariable String id) {
        return R.success(payChannelService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增支付渠道")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody PayChannelBo bo) {
        return R.success(payChannelService.savePayChannel(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改支付渠道")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody PayChannelBo bo) {
        return R.success(payChannelService.updatePayChannel(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除支付渠道")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(payChannelService.deletePayChannel(ids));
    }

    private final PayChannelService payChannelService;

}
