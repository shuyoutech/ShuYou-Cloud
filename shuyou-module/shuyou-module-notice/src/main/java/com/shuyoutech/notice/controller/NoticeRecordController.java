package com.shuyoutech.notice.controller;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.notice.domain.bo.NoticeRecordBo;
import com.shuyoutech.notice.domain.vo.NoticeRecordVo;
import com.shuyoutech.notice.service.NoticeRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("noticeRecord")
@Tag(name = "NoticeRecordController", description = "通知记录管理API控制器")
public class NoticeRecordController {

    @PostMapping("page")
    @Operation(description = "通知记录分页列表")
    public R<PageResult<NoticeRecordVo>> page(@RequestBody PageQuery<NoticeRecordBo> pageQuery) {
        return R.success(noticeRecordService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询通知记录详情")
    public R<NoticeRecordVo> detail(@PathVariable String id) {
        return R.success(noticeRecordService.detail(id));
    }

    private final NoticeRecordService noticeRecordService;

}
