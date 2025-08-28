package com.shuyoutech.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.service.ApiCommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.shuyoutech.common.core.constant.CommonConstants.CHARSET_UTF_8;

/**
 * @author YangChao
 * @date 2025-08-19 13:25
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "ApiController", description = "统一入口API控制器")
public class ApiController {

    @PostMapping(value = "/v1/interface")
    @Operation(summary = "通用接口")
    public void interfaceV1(@RequestParam(required = false) Map<String, String> params, //
                            @RequestBody(required = false) JSONObject body, //
                            @RequestHeader Map<String, String> headers, //
                            HttpServletResponse response) {
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        apiCommonService.interfaceV1(body, response);
    }

    private final ApiCommonService apiCommonService;

}
