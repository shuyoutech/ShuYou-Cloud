package com.shuyoutech.api.service.aigc.provider;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.api.init.ApiRunner.MEDIA_TYPE_JSON;
import static com.shuyoutech.api.init.ApiRunner.OK_HTTP_CLIENT;
import static com.shuyoutech.common.core.constant.CommonConstants.HEADER_AUTHORIZATION_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <a href="https://docs.dev.runwayml.com/api/">专业创作首选，适合影视级精细编辑用户(如复杂场景、人物表情优化)、专业影视创作者(需高稳定性)</a>
 *
 * @author YangChao
 * @date 2025-07-13 20:18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RunwayProvider implements ModelProvider {

    @Override
    public String providerName() {
        return AiProviderTypeEnum.RUNWAY.getValue();
    }

    @Override
    public Request buildRequest(String url, String apiKey, String body) {
        RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);
        return new Request.Builder() //
                .url(url)//
                .post(requestBody) //
                .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                .addHeader(AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                .addHeader(HEADER_RUNWAY_X_RUNWAY_VERSION, HEADER_RUNWAY_X_RUNWAY_VERSION_VALUE) //
                .build();
    }

    @Override
    public void textToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + RUNWAY_TEXT_TO_IMAGE;
            Request request = buildRequest(url, apiKey, body.toJSONString());
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealTaskResponse(baseUrl, apiKey, res, response);
        } catch (Exception e) {
            log.error("textToImage runway ===================== exception:{}", e.getMessage());
        }
    }

    @Override
    public void imageToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        textToImage(baseUrl, apiKey, body, response);
    }

    @Override
    public void multiImageToImage(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        textToImage(baseUrl, apiKey, body, response);
    }

    /**
     * 处理返回结果
     */
    private void dealTaskResponse(String baseUrl, String apiKey, Response res, HttpServletResponse response) {
        try {
            String bodyStr = new String(res.body().bytes(), StandardCharsets.UTF_8);
            log.info("dealTaskResponse runway ====================== data:{}", bodyStr);
            if (!res.isSuccessful()) {
                response.setStatus(res.code());
                PrintWriter writer = response.getWriter();
                writer.write(bodyStr);
                writer.flush();
                return;
            }
            ThreadUtil.sleep(5000);
            JSONObject bodyObject = JSONObject.parseObject(bodyStr);
            String taskId = bodyObject.getString("id");
            if (StringUtils.isEmpty(taskId)) {
                return;
            }
            JSONObject taskResult;
            String status;
            for (int i = 0; i < 60; i++) {
                taskResult = getTask(baseUrl, apiKey, taskId);
                if (null == taskResult) {
                    break;
                }
                status = taskResult.getString("status");
                if ("SUCCEEDED".equalsIgnoreCase(status)) {
                    List<JSONObject> datalist = CollectionUtils.newArrayList();
                    JSONArray results = taskResult.getJSONArray("output");
                    for (int j = 0; j < results.size(); j++) {
                        JSONObject image = new JSONObject();
                        image.put("type", "url");
                        image.put("url", results.getString(j));
                        datalist.add(image);
                    }
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(R.success(datalist)));
                    writer.flush();
                    break;
                }
                if ("PENDING".equalsIgnoreCase(status) || "RUNNING".equalsIgnoreCase(status)) {
                    ThreadUtil.sleep(5000);
                    continue;
                }
                break;
            }
        } catch (Exception e) {
            log.error("dealTaskResponse runway ===================== exception:{}", e.getMessage());
        }
    }

    private JSONObject getTask(String baseUrl, String apiKey, String taskId) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder() //
                    .connectTimeout(20, TimeUnit.SECONDS) // 20秒
                    .readTimeout(30, TimeUnit.SECONDS) // 30秒
                    .writeTimeout(30, TimeUnit.SECONDS) // 30秒
                    .build();

            Request request = new Request.Builder() //
                    .url(baseUrl + RUNWAY_TASKS + taskId) //
                    .get() //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .addHeader(HEADER_RUNWAY_X_RUNWAY_VERSION, HEADER_RUNWAY_X_RUNWAY_VERSION_VALUE) //
                    .build();

            Response res = client.newCall(request).execute();
            if (res.isSuccessful()) {
                String bodyStr = new String(res.body().bytes(), StandardCharsets.UTF_8);
                log.info("getTask runway ====================== data:{}", bodyStr);
                return JSONObject.parseObject(bodyStr);
            } else {
                log.error("getTask runway ===================== code:{},response:{}", res.code(), res.message());
            }
        } catch (Exception e) {
            log.error("getTask runway ===================== exception:{}", e.getMessage());
        }
        return null;
    }

}
