package com.shuyoutech.api.service.aigc.listener;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import static com.shuyoutech.api.constant.AiConstants.EVENT_STREAM_DATA;
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
    private HttpServletResponse response;
    private StringBuilder contentSb = new StringBuilder();
    private StringBuilder dataSb = new StringBuilder();
    private String provider;

    public SSEChatEventListener(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        countDownLatch.countDown();
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        try {
            response.getWriter().write(EVENT_STREAM_DATA + data);
            response.getWriter().println();
            response.getWriter().println();
            response.getWriter().flush();
        } catch (Exception e) {
            log.error("onEvent ====================== exception:{}", e.getMessage());
        }
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response res) {
        if (null != res) {
            try {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(res.code());
                String body = new String(res.body().bytes(), StandardCharsets.UTF_8);
                response.getWriter().write(body);
                response.getWriter().flush();
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
