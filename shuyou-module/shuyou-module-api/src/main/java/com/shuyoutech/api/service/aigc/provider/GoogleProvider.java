package com.shuyoutech.api.service.aigc.provider;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
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

import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.api.init.ApiRunner.MEDIA_TYPE_JSON;
import static com.shuyoutech.api.init.ApiRunner.OK_HTTP_CLIENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @author YangChao
 * @date 2025-08-22 12:19
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleProvider implements ModelProvider {

    @Override
    public String providerName() {
        return AiProviderTypeEnum.GOOGLE.getValue();
    }

    @Override
    public Request buildRequest(String url, String apiKey, String body) {
        RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);
        return new Request.Builder() //
                .url(url)//
                .post(requestBody) //
                .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                .addHeader(HEADER_GOOGLE_X_API_KEY, apiKey) //
                .build();
    }

    @Override
    public void chatCompletion(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + OPENAI_CHAT_COMPLETIONS;
            Request request = buildRequest(url, apiKey, body.toJSONString());
            if (BooleanUtils.isFalse(body.getBooleanValue(STREAM, false))) {
                response.setContentType(APPLICATION_JSON_VALUE);
                Response res = OK_HTTP_CLIENT.newCall(request).execute();
                dealResponse(res, response);
            } else {
                response.setContentType(TEXT_EVENT_STREAM_VALUE);
                SSEChatEventListener sseChatEventListener = new SSEChatEventListener(response);
                EventSource.Factory factory = EventSources.createFactory(OK_HTTP_CLIENT);
                factory.newEventSource(request, sseChatEventListener);
                boolean await = sseChatEventListener.getCountDownLatch().await(5, TimeUnit.MINUTES);
                if (!await) {
                    log.error("chatCompletion google ==================== getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("chatCompletion google ===================== exception:{}", e.getMessage());
        }
    }
}
