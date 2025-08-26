package com.shuyoutech.api.service.aigc.provider;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.api.service.aigc.listener.SSEChatEventListener;
import com.shuyoutech.common.core.util.BooleanUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.OPENAI_CHAT_COMPLETIONS;
import static com.shuyoutech.api.constant.AiConstants.STREAM;
import static com.shuyoutech.api.init.ApiRunner.OK_HTTP_CLIENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * <a href="https://docs.x.ai/docs/api-reference">role:developer,user,assistant,tool</a>
 *
 * @author YangChao
 * @date 2025-07-13 20:18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class XAIProvider implements ModelProvider {

    @Override
    public String providerName() {
        return AiProviderTypeEnum.XAI.getValue();
    }

    @Override
    public void chatCompletion(String baseUrl, String apiKey, String body, HttpServletResponse response) {
        try {
            JSONObject bodyJson = JSONObject.parseObject(body);
            String url = baseUrl + OPENAI_CHAT_COMPLETIONS;
            Request request = buildRequest(url, apiKey, body);
            if (BooleanUtils.isFalse(bodyJson.getBooleanValue(STREAM, false))) {
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
                    log.error("chatCompletion xai ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("chatCompletion xai ===================== exception:{}", e.getMessage());
        }
    }

}
