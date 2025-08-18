package com.shuyoutech.notice.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.notice.domain.bo.SmsTemplateBo;
import com.shuyoutech.notice.domain.vo.SmsTemplateVo;
import com.shuyoutech.notice.service.SmsTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("smsTemplate")
@Tag(name = "SmsTemplateController", description = "短信模板管理API控制器")
public class SmsTemplateController {

    @PostMapping("page")
    @Operation(description = "短信模板分页列表")
    public R<PageResult<SmsTemplateVo>> page(@RequestBody PageQuery<SmsTemplateBo> pageQuery) {
        return R.success(smsTemplateService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询短信模板详情")
    public R<SmsTemplateVo> detail(@PathVariable String id) {
        return R.success(smsTemplateService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增短信模板")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody SmsTemplateBo bo) {
        return R.success(smsTemplateService.saveSmsTemplate(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改短信模板")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody SmsTemplateBo bo) {
        return R.success(smsTemplateService.updateSmsTemplate(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除短信模板")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(smsTemplateService.deleteSmsTemplate(ids));
    }

    private final SmsTemplateService smsTemplateService;

}
