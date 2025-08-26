package com.shuyoutech.api.controller;

import com.shuyoutech.api.service.ApiService;
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

    @PostMapping(value = "/v1/image/textToImage")
    @Operation(summary = "文生图接口")
    public void textToImage(HttpServletRequest request, HttpServletResponse response) {
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

    @PostMapping(value = "/v1/services/embeddings/multimodal-embedding/multimodal-embedding")
    @Operation(summary = "多模态向量接口", description = "多模态向量模型将文本、图像或视频转换成一组由浮点数组成的向量，适用于视频分类、图像分类、图文检索等")
    public void multimodalEmbedding(HttpServletRequest request, HttpServletResponse response) {
        apiAigcService.embedding(request, response);
    }

    private final ApiService apiAigcService;

}
