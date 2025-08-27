package com.shuyoutech.aigc.provider.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.aigc.domain.model.ChatMessage;
import com.shuyoutech.aigc.domain.model.ChatModelBuilder;
import com.shuyoutech.aigc.domain.model.UserModelUsage;
import com.shuyoutech.aigc.listener.SSEChatEventListener;
import com.shuyoutech.aigc.provider.service.ModelService;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapUtils;
import com.shuyoutech.common.core.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.aigc.provider.AigcModelFactory.MEDIA_TYPE_JSON;
import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.common.core.constant.CommonConstants.HEADER_AUTHORIZATION_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <a href="https://platform.moonshot.cn/docs/api/chat">Moonshot</a>
 *
 * @author YangChao
 * @date 2025-07-13 20:18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class MoonshotModelService implements ModelService {

    @Override
    public String providerName() {
        return AiProviderTypeEnum.MOONSHOT.getValue();
    }

    @Override
    public void chat(ChatModelBuilder builder, HttpServletResponse response) {
        try {
            UserModelUsage userToken = builder.getUserToken();
            JSONObject modelParam = builder.getModelParam();
            Map<String, Object> paramMap = MapUtils.newHashMap();
            paramMap.put("model", builder.getModelName());
            // role: system、user、assistant、tool
            List<ChatMessage> messages = CollectionUtils.newArrayList();
            String prompt = modelParam.getString("prompt");
            if (StringUtils.isNotBlank(prompt)) {
                messages.add(ChatMessage.builder().role(ROLE_SYSTEM).content(prompt).build());
            }
            if (CollectionUtils.isNotEmpty(userToken.getMessages())) {
                messages.addAll(userToken.getMessages());
            }
            messages.add(ChatMessage.builder().role(ROLE_USER).content(modelParam.getString("message")).build());
            paramMap.put("messages", messages);
            paramMap.put("stream", true);
            paramMap.put("max_tokens", modelParam.get("max_tokens"));
            paramMap.put("temperature", modelParam.get("temperature"));
            paramMap.put("top_p", modelParam.get("top_p"));
            paramMap.put("n", modelParam.get("n"));
            paramMap.put("presence_penalty", modelParam.get("presence_penalty"));
            paramMap.put("frequency_penalty", modelParam.get("frequency_penalty"));
            paramMap.put("response_format", modelParam.get("response_format"));
            paramMap.put("stop", modelParam.get("stop"));
            paramMap.put("tools", modelParam.get("tools"));
            String requestBody = JSONObject.toJSONString(paramMap);
            log.info("chat ============================ moonshot request:{}", requestBody);
            RequestBody body = RequestBody.create(requestBody, MEDIA_TYPE_JSON);

            OkHttpClient client = new OkHttpClient().newBuilder() //
                    .connectTimeout(3, TimeUnit.MINUTES) // 3分
                    .readTimeout(5, TimeUnit.MINUTES) // 5分
                    .writeTimeout(5, TimeUnit.MINUTES) // 5分
                    .build();

            Request request = new Request.Builder() //
                    .url(builder.getBaseUrl() + OPENAI_CHAT_COMPLETIONS)//
                    .post(body) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + builder.getApiKey()) //
                    .build();

            userToken.setRequestBody(requestBody);
            userToken.setEnableMemory(modelParam.getBooleanValue("enable_memory", false));
            userToken.setEnableThinking(modelParam.getBooleanValue("enable_thinking", false));
            userToken.setEnableSearch(modelParam.getBooleanValue("enable_search", false));
            SSEChatEventListener sseChatEventListener = new SSEChatEventListener(userToken, response);
            EventSource.Factory factory = EventSources.createFactory(client);
            factory.newEventSource(request, sseChatEventListener);
            boolean await = sseChatEventListener.getCountDownLatch().await(5, TimeUnit.MINUTES);
            if (!await) {
                log.error("chat moonshot ============================ getCountDownLatch timed out");
            }
        } catch (Exception e) {
            log.error("chat moonshot ===================== exception:{}", e.getMessage());
        }
    }

}
