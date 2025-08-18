package com.shuyoutech.notice.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.web.annotation.RateLimiter;
import com.shuyoutech.common.web.enums.LimitTypeEnum;
import com.shuyoutech.notice.domain.bo.NoticeSendBo;
import com.shuyoutech.notice.service.NoticeService;
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
 * @date 2025-07-19 11:51
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
@Tag(name = "NoticeController", description = "通知服务API控制器")
public class NoticeController {

    @RateLimiter(key = "noticeSend", count = 1, limitType = LimitTypeEnum.IP)
    @PostMapping("send")
    @Operation(description = "发送短信|邮件")
    public R<Void> send(@Validated @RequestBody NoticeSendBo bo) {
        noticeService.send(bo);
        return R.success();
    }

    private final NoticeService noticeService;

}
