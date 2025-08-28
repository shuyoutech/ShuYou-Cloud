package com.shuyoutech.api.service.aigc.provider;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static com.shuyoutech.api.init.ApiRunner.MEDIA_TYPE_JSON;
import static com.shuyoutech.common.core.constant.CommonConstants.HEADER_AUTHORIZATION_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 模型公共的API功能
 *
 * @author YangChao
 * @date 2025-07-13 20:07
 **/
public interface ModelProvider {

    String providerName();

    default Request buildRequest(String url, String apiKey, String body) {
        RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);
        return new Request.Builder() //
                .url(url) //
                .post(requestBody) //
                .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                .build();
    }

    default void dealResponse(Response res, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            String bodyStr = new String(res.body().bytes(), StandardCharsets.UTF_8);
            response.setStatus(res.code());
            writer.write(bodyStr);
            writer.flush();
        } catch (Exception ignored) {
        }
    }

    default void chatCompletion(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void betaCompletion(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void textToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void imageToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void multiImageToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void audioSpeech(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void audioTranscription(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void audioTranslation(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void embedding(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void multimodalEmbedding(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

    default void moderation(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
    }

}
