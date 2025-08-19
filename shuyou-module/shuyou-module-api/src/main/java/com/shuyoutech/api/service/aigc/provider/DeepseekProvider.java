package com.shuyoutech.api.service.aigc.provider;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.service.aigc.listener.SSEChatEventListener;
import com.shuyoutech.common.core.util.BooleanUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.api.init.ApiRunner.MEDIA_TYPE_JSON;
import static com.shuyoutech.api.init.ApiRunner.OK_HTTP_CLIENT;
import static com.shuyoutech.common.core.constant.CommonConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * <a href="https://api-docs.deepseek.com/zh-cn/"></a>
 * <a href="https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-deepseek/src/test/java/org/springframework/ai/deepseek/api/DeepSeekApiIT.java"></a>
 * <a href="https://docs.spring.io/spring-ai/reference/api/chat/deepseek-chat.html"></a>
 *
 * @author YangChao
 * @date 2025-07-13 20:18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DeepseekProvider {

    private String baseUrl;
    private String apiKey;

    public DeepseekProvider(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public void chatCompletion(String body, HttpServletResponse response) {
        try {
            JSONObject bodyJson = JSONObject.parseObject(body);
            RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);

            Request request = new Request.Builder() //
                    .url(baseUrl + DEEPSEEK_CHAT_COMPLETIONS)//
                    .post(requestBody) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            if (BooleanUtils.isFalse(bodyJson.getBooleanValue(STREAM, false))) {
                response.setContentType(APPLICATION_JSON_VALUE);
                Response res = OK_HTTP_CLIENT.newCall(request).execute();
                dealResponse(res, response);
            } else {
                response.setContentType(TEXT_EVENT_STREAM_VALUE);
                response.setHeader(CACHE_CONTROL, NO_CACHE);
                SSEChatEventListener sseChatEventListener = new SSEChatEventListener(response);
                EventSource.Factory factory = EventSources.createFactory(OK_HTTP_CLIENT);
                factory.newEventSource(request, sseChatEventListener);
                boolean await = sseChatEventListener.getCountDownLatch().await(5, TimeUnit.MINUTES);
                if (!await) {
                    log.error("chatCompletion deepseek ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("chatCompletion deepseek ===================== exception:{}", e.getMessage());
        }
    }

    public void betaCompletion(String body, HttpServletResponse response) {
        try {
            JSONObject bodyJson = JSONObject.parseObject(body);
            RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);

            Request request = new Request.Builder() //
                    .url(baseUrl + DEEPSEEK_BETA_COMPLETIONS)//
                    .post(requestBody) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            if (BooleanUtils.isFalse(bodyJson.getBooleanValue(STREAM, false))) {
                response.setContentType(APPLICATION_JSON_VALUE);
                Response res = OK_HTTP_CLIENT.newCall(request).execute();
                dealResponse(res, response);
            } else {
                response.setContentType(TEXT_EVENT_STREAM_VALUE);
                response.setHeader(CACHE_CONTROL, NO_CACHE);
                SSEChatEventListener sseChatEventListener = new SSEChatEventListener(response);
                EventSource.Factory factory = EventSources.createFactory(OK_HTTP_CLIENT);
                factory.newEventSource(request, sseChatEventListener);
                boolean await = sseChatEventListener.getCountDownLatch().await(5, TimeUnit.MINUTES);
                if (!await) {
                    log.error("betaCompletion deepseek ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("betaCompletion deepseek ===================== exception:{}", e.getMessage());
        }
    }

    private void dealResponse(Response res, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            String bodyStr = new String(res.body().bytes(), StandardCharsets.UTF_8);
            response.setStatus(res.code());
            writer.write(bodyStr);
            writer.flush();
        } catch (Exception e) {
            log.error("dealResponse deepseek ===================== exception:{}", e.getMessage());
        }
    }

}
