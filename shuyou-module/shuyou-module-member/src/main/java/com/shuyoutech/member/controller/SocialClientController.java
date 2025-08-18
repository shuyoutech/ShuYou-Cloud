package com.shuyoutech.member.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.member.domain.bo.SocialClientBo;
import com.shuyoutech.member.domain.vo.SocialClientVo;
import com.shuyoutech.member.service.SocialClientService;
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

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-21 16:59:07
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("socialClient")
@Tag(name = "SysSocialClientController", description = "社交客户端管理API控制器")
public class SocialClientController {

    @PostMapping("page")
    @Operation(description = "社交客户端分页列表")
    public R<PageResult<SocialClientVo>> page(@RequestBody PageQuery<SocialClientBo> pageQuery) {
        return R.success(socialClientService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询社交客户端详情")
    public R<SocialClientVo> detail(@PathVariable String id) {
        return R.success(socialClientService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增社交客户端")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody SocialClientBo bo) {
        return R.success(socialClientService.saveSysSocialClient(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改社交客户端")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody SocialClientBo bo) {
        return R.success(socialClientService.updateSysSocialClient(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除社交客户端")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(socialClientService.deleteSysSocialClient(ids));
    }

    @PostMapping(path = "status")
    @Operation(description = "状态修改")
    public R<Boolean> status(@Validated({StatusGroup.class}) @RequestBody SocialClientBo bo) {
        return R.success(socialClientService.statusSysSocialClient(bo.getId(), bo.getStatus()));
    }

    private final SocialClientService socialClientService;

}
