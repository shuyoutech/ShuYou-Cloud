package com.shuyoutech.api.controller;

import com.shuyoutech.api.service.aigc.ApiAigcService;
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
@Tag(name = "ApiAigcController", description = "AIGC API控制器")
public class ApiAigcController {

    @PostMapping(value = "/v1/chat/completions")
    @Operation(summary = "对话补全接口")
    public void chatCompletions(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        apiAigcService.chatCompletion(request, response);
    }

    @PostMapping(value = "/v1/beta/completions")
    @Operation(summary = "FIM补全接口")
    public void betaCompletions(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setStatus(HttpStatus.OK.value());
        apiAigcService.betaCompletion(request, response);
    }

    @PostMapping(value = "/v1/embeddings")
    @Operation(summary = "文本向量化接口")
    public void embedding(HttpServletRequest request, HttpServletResponse response) {
        apiAigcService.embedding(request, response);
    }

    private final ApiAigcService apiAigcService;

}
