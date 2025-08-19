package com.shuyoutech.aigc.controller;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.aigc.domain.bo.AigcUsageBo;
import com.shuyoutech.aigc.service.AigcUsageService;
import io.milvus.param.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-13 15:37
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("aigc")
@Tag(name = "AigcUsageController", description = "AI应用使用统计 API控制器")
public class AigcUsageController {

    @PostMapping(value = "usage")
    @Operation(summary = "AI使用统计接口")
    public R<List<JSONObject>> usage(@RequestBody AigcUsageBo bo) {
        return R.success(aigcUsageService.usage(bo.getDate()));
    }

    private final AigcUsageService aigcUsageService;

}
