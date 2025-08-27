package com.shuyoutech.aigc.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.aigc.domain.model.TokenUsage;
import com.shuyoutech.aigc.domain.model.UserModelUsage;
import com.shuyoutech.api.constant.AiConstants;
import com.shuyoutech.common.core.util.NumberUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.disruptor.model.DisruptorData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.common.disruptor.init.DisruptorRunner.disruptorProducer;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author YangChao
 * @date 2025-07-15 13:57
 **/
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SSEChatEventListener extends EventSourceListener {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private UserModelUsage userModelUsage;
    private HttpServletResponse response;
    private WebSocketSession webSocketSession;
    private StringBuilder contentSb = new StringBuilder();
    private StringBuilder dataSb = new StringBuilder();
    private TokenUsage tokenUsage;
    private String provider;

    public SSEChatEventListener(UserModelUsage userModelUsage, HttpServletResponse response) {
        this.userModelUsage = userModelUsage;
        this.response = response;
        this.provider = userModelUsage.getProvider();
        this.tokenUsage = TokenUsage.builder().inputTokenCount(0).outputTokenCount(0).totalTokenCount(0).build();
    }

    public SSEChatEventListener(UserModelUsage userModelUsage, WebSocketSession webSocketSession) {
        this.userModelUsage = userModelUsage;
        this.webSocketSession = webSocketSession;
        this.provider = userModelUsage.getProvider();
        this.tokenUsage = TokenUsage.builder().inputTokenCount(0).outputTokenCount(0).totalTokenCount(0).build();
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        try {
            // 设置消耗时间
            Date end = new Date();
            long costTime = end.getTime() - userModelUsage.getRequestTime().getTime();
            userModelUsage.setResponseTime(end);
            userModelUsage.setDurationSeconds(NumberUtils.div(String.valueOf(costTime), "1000", 2));
            if (null != tokenUsage) {
                userModelUsage.setInputTokenCount(tokenUsage.getInputTokenCount());
                userModelUsage.setOutputTokenCount(tokenUsage.getOutputTokenCount());
                userModelUsage.setTotalTokenCount(tokenUsage.getTotalTokenCount());
            }
            userModelUsage.setAssistantMessage(contentSb.toString());
            userModelUsage.setResponseBody(dataSb.toString());
            DisruptorData disruptorData = new DisruptorData();
            disruptorData.setServiceName(AiConstants.USER_CHAT_SERVICE);
            disruptorData.setData(userModelUsage);
            disruptorProducer.pushData(disruptorData);

            JSONObject json = new JSONObject();
            json.put("conversationId", userModelUsage.getConversationId());
            json.put("inputTokenCount", userModelUsage.getInputTokenCount());
            json.put("outputTokenCount", userModelUsage.getOutputTokenCount());
            json.put("durationSeconds", userModelUsage.getDurationSeconds());
            json.put("totalLength", contentSb.length());
            dealResponse(EVENT_END, json.toString());
            countDownLatch.countDown();
        } catch (Exception e) {
            log.error("onClosed ====================== exception:{}", e.getMessage());
        }
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        try {
            dataSb.append(data).append("\n");
            if (!JSON.isValidObject(data)) {
                return;
            }
            JSONObject object = JSONObject.parseObject(data);
            dealUsage(object, tokenUsage);
            dealContent(object);
        } catch (Exception e) {
            log.error("onEvent ====================== exception:{}", e.getMessage());
        }
    }

    private void dealUsage(JSONObject data, TokenUsage tokenUsage) {
        JSONObject usage = null;
        if (data.containsKey("usage")) {
            usage = data.getJSONObject("usage");
        } else if (data.containsKey("choices") && data.getJSONArray("choices").getJSONObject(0).containsKey("usage")) {
            usage = data.getJSONArray("choices").getJSONObject(0).getJSONObject("usage");
        } else if (data.containsKey("usageMetadata")) {
            usage = data.getJSONObject("usageMetadata");
        }
        if (null == usage || usage.isEmpty()) {
            return;
        }
        if (usage.containsKey("prompt_tokens")) {
            tokenUsage.setInputTokenCount(usage.getIntValue("prompt_tokens", 0));
        } else if (usage.containsKey("input_tokens")) {
            tokenUsage.setInputTokenCount(usage.getIntValue("input_tokens", 0));
        } else if (usage.containsKey("promptTokenCount")) {
            tokenUsage.setInputTokenCount(usage.getIntValue("promptTokenCount", 0));
        }
        if (usage.containsKey("completion_tokens")) {
            tokenUsage.setOutputTokenCount(usage.getIntValue("completion_tokens", 0));
        } else if (usage.containsKey("output_tokens")) {
            tokenUsage.setOutputTokenCount(usage.getIntValue("output_tokens", 0));
        } else if (usage.containsKey("candidatesTokenCount")) {
            tokenUsage.setOutputTokenCount(usage.getIntValue("candidatesTokenCount", 0));
        }
        if (usage.containsKey("total_tokens")) {
            tokenUsage.setTotalTokenCount(usage.getIntValue("total_tokens", 0));
        } else if (usage.containsKey("totalTokenCount")) {
            tokenUsage.setTotalTokenCount(usage.getIntValue("totalTokenCount", 0));
        } else {
            tokenUsage.setTotalTokenCount(tokenUsage.getInputTokenCount() + tokenUsage.getOutputTokenCount());
        }
    }

    private void dealContent(JSONObject data) {
        String content = null;
        if (data.containsKey("choices")) {
            JSONArray choices = data.getJSONArray("choices");
            if (null == choices || choices.isEmpty()) {
                return;
            }
            JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
            if (null == delta) {
                return;
            }
            content = delta.getString("content");
        } else if (data.containsKey("candidates")) {
            JSONArray candidates = data.getJSONArray("candidates");
            if (null == candidates || candidates.isEmpty()) {
                return;
            }
            JSONObject contentJson = candidates.getJSONObject(0).getJSONObject("content");
            if (null == contentJson || contentJson.getJSONArray("parts").isEmpty()) {
                return;
            }
            JSONArray parts = contentJson.getJSONArray("parts");
            JSONObject part = parts.getJSONObject(0);
            content = part.getString("text");
        } else if (data.containsKey("delta")) {
            JSONObject delta = data.getJSONObject("delta");
            if (null == delta || delta.isEmpty()) {
                return;
            }
            if (StringUtils.isNotBlank(delta.getString("text"))) {
                content = delta.getString("text");
            } else if (StringUtils.isNotBlank(delta.getString("thinking"))) {
                content = delta.getString("thinking");
            } else if (StringUtils.isNotBlank(delta.getString("partial_json"))) {
                content = delta.getString("partial_json");
            }
        }
        if (StringUtils.isBlank(content)) {
            return;
        }
        contentSb.append(content);
        JSONObject json = new JSONObject();
        json.put("content", content);
        dealResponse(EVENT_ANSWER, json.toJSONString());
    }

    private void dealResponse(String event, String data) {
        try {
            if (null != response) {
                response.getWriter().write("event:" + event);
                response.getWriter().println();
                response.getWriter().write("data: " + data);
                response.getWriter().println();
                response.getWriter().println();
                response.getWriter().flush();
            } else {
                JSONObject object = new JSONObject();
                object.put("event", EVENT_ANSWER);
                object.put("data", data);
                webSocketSession.sendMessage(new TextMessage(object.toJSONString()));
            }
        } catch (Exception e) {
            log.error("dealResponse ============== exception:{}", e.getMessage());
        }
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response res) {
        if (null != res) {
            try {
                String body = new String(res.body().bytes(), StandardCharsets.UTF_8);
                JSONObject object = new JSONObject();
                object.put("code", res.code());
                object.put("message", res.message());
                object.put("body", body);
                if (null != response) {
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(res.code());
                    response.getWriter().write(body);
                    response.getWriter().flush();
                } else {
                    JSONObject object2 = new JSONObject();
                    object2.put("event", EVENT_ERROR);
                    object2.put("data", body);
                    webSocketSession.sendMessage(new TextMessage(object2.toJSONString()));
                }
                dataSb.append(object.toJSONString());
            } catch (Exception e) {
                log.error("onFailure ====================== exception:{}", e.getMessage());
            }
        }
        countDownLatch.countDown();
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
    }

}
