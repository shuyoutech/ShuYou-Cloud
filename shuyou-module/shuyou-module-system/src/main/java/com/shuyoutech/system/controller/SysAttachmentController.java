package com.shuyoutech.system.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.system.domain.bo.SysAttachmentBo;
import com.shuyoutech.system.domain.vo.SysAttachmentVo;
import com.shuyoutech.system.service.SysAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-09-03 13:17:59
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("attachment")
@Tag(name = "SysAttachmentController", description = "附件管理API控制器")
public class SysAttachmentController {

    @PostMapping("page")
    @Operation(description = "附件分页列表")
    public R<PageResult<SysAttachmentVo>> page(@RequestBody PageQuery<SysAttachmentBo> pageQuery) {
        return R.success(sysAttachmentService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询附件详情")
    public R<SysAttachmentVo> detail(@PathVariable String id) {
        return R.success(sysAttachmentService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增附件")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody SysAttachmentBo bo) {
        return R.success(sysAttachmentService.saveSysAttachment(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改附件")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody SysAttachmentBo bo) {
        return R.success(sysAttachmentService.updateSysAttachment(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除附件")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(sysAttachmentService.deleteSysAttachment(ids));
    }

    private final SysAttachmentService sysAttachmentService;

}
