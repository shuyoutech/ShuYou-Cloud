package com.shuyoutech.api.service.aigc.provider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.multipart.MultipartFormData;
import cn.hutool.core.net.multipart.UploadFile;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.api.service.aigc.listener.SSEChatEventListener;
import com.shuyoutech.common.core.util.BooleanUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.*;
import static com.shuyoutech.api.init.ApiRunner.MEDIA_TYPE_JSON;
import static com.shuyoutech.api.init.ApiRunner.OK_HTTP_CLIENT;
import static com.shuyoutech.common.core.constant.CommonConstants.HEADER_AUTHORIZATION_PREFIX;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * <a href="https://github.com/openai/openai-java"></a>
 * <a href="https://platform.openai.com/docs/api-reference"></a>
 *
 * @author YangChao
 * @date 2025-07-13 20:18
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiProvider implements ModelProvider {

    @Override
    public String providerName() {
        return AiProviderTypeEnum.OPENAI.getValue();
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
                    log.error("chatCompletion openai ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("chatCompletion openai ===================== exception:{}", e.getMessage());
        }
    }

    public void imageGeneration(String body, HttpServletResponse response) {
        try {
            JSONObject bodyJson = JSONObject.parseObject(body);
            Request request = this.buildRequest(body, OPENAI_IMAGES_GENERATIONS);
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
                    log.error("imageGeneration openai ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("imageGeneration openai ===================== exception:{}", e.getMessage());
        }
    }

    public void audioSpeech(String body, HttpServletResponse response) {
        try {
            Request request = this.buildRequest(body, OPENAI_AUDIO_SPEECH);
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioSpeech openai ===================== exception:{}", e.getMessage());
        }
    }

    public void audioTranscription(MultipartFormData multipart, HttpServletResponse response) {
        try {
            UploadFile file = multipart.getFile("file");
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart("model", multipart.getParam("model"));
            bodyBuilder.addFormDataPart("file", file.getFileName(), RequestBody.create(file.getFileContent(), MediaType.get(FileUtil.getMimeType(file.getFileName()))));
            bodyBuilder.addFormDataPart("language", multipart.getParam("language"));
            bodyBuilder.addFormDataPart("prompt", multipart.getParam("prompt"));
            bodyBuilder.addFormDataPart("response_format", multipart.getParam("response_format"));
            MultipartBody body = bodyBuilder.build();

            Request request = new Request.Builder() //
                    .url(baseUrl + OPENAI_AUDIO_TRANSCRIPTIONS) //
                    .post(body) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioTranslation openai ===================== exception:{}", e.getMessage());
        }
    }

    public void audioTranslation(MultipartFormData multipart, HttpServletResponse response) {
        try {
            UploadFile file = multipart.getFile("file");
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart("model", multipart.getParam("model"));
            bodyBuilder.addFormDataPart("file", file.getFileName(), RequestBody.create(file.getFileContent(), MediaType.get(FileUtil.getMimeType(file.getFileName()))));
            bodyBuilder.addFormDataPart("prompt", multipart.getParam("prompt"));
            bodyBuilder.addFormDataPart("response_format", multipart.getParam("response_format"));
            bodyBuilder.addFormDataPart("temperature", multipart.getParam("temperature"));
            MultipartBody body = bodyBuilder.build();

            Request request = new Request.Builder() //
                    .url(baseUrl + OPENAI_AUDIO_TRANSLATIONS) //
                    .post(body) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioTranslation openai ===================== exception:{}", e.getMessage());
        }
    }

    public void embedding(String body, HttpServletResponse response) {
        try {
            Request request = this.buildRequest(body, OPENAI_EMBEDDINGS);
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("embedding openai ===================== exception:{}", e.getMessage());
        }
    }

    public void moderation(String body, HttpServletResponse response) {
        try {
            Request request = this.buildRequest(body, OPENAI_MODERATIONS);
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("moderation openai ===================== exception:{}", e.getMessage());
        }
    }

}
