package com.shuyoutech.notice.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.notice.domain.bo.EmailTemplateBo;
import com.shuyoutech.notice.domain.vo.EmailTemplateVo;
import com.shuyoutech.notice.service.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("emailTemplate")
@Tag(name = "EmailTemplateController", description = "电子邮件模板管理API控制器")
public class EmailTemplateController {

    @PostMapping("page")
    @Operation(description = "电子邮件模板分页列表")
    public R<PageResult<EmailTemplateVo>> page(@RequestBody PageQuery<EmailTemplateBo> pageQuery) {
        return R.success(emailTemplateService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询电子邮件模板详情")
    public R<EmailTemplateVo> detail(@PathVariable String id) {
        return R.success(emailTemplateService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增电子邮件模板")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody EmailTemplateBo bo) {
        return R.success(emailTemplateService.saveEmailTemplate(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改电子邮件模板")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody EmailTemplateBo bo) {
        return R.success(emailTemplateService.updateEmailTemplate(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除电子邮件模板")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(emailTemplateService.deleteEmailTemplate(ids));
    }

    private final EmailTemplateService emailTemplateService;

}
