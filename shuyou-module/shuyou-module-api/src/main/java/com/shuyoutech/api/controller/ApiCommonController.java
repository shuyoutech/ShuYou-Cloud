package com.shuyoutech.api.controller;

import com.shuyoutech.api.service.ApiCommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shuyoutech.common.core.constant.CommonConstants.CHARSET_UTF_8;

/**
 * @author YangChao
 * @date 2025-08-19 13:25
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "ApiCommonController", description = "通用API控制器")
public class ApiCommonController {

    @PostMapping(value = "/v1/common/interface")
    @Operation(summary = "通用接口")
    public void commonInterfaceV1(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        apiCommonService.commonInterfaceV1(request, response);
    }

    private final ApiCommonService apiCommonService;

}
