package com.shuyoutech.api.service.aigc.provider;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.api.model.RemoteSysFile;
import com.shuyoutech.api.service.RemoteSystemService;
import com.shuyoutech.api.service.aigc.listener.SSEChatEventListener;
import com.shuyoutech.common.core.util.BooleanUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.shuyoutech.api.constant.AiConstants.*;
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

    /**
     * 对话聊天
     */
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
                    log.error("chatCompletion openai ====================  getCountDownLatch timed out");
                }
            }
        } catch (Exception e) {
            log.error("chatCompletion openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 图片生成
     */
    public void imageGeneration(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + OPENAI_IMAGES_GENERATIONS;
            Request request = this.buildRequest(url, apiKey, body.toJSONString());
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("imageGeneration openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 文本转换为音频
     */
    @Override
    public void audioSpeech(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + OPENAI_AUDIO_SPEECH;
            Request request = this.buildRequest(url, apiKey, body.toJSONString());
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioSpeech openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 将音频转换为语言文本
     */
    @Override
    public void audioTranscription(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            RemoteSysFile file = remoteSystemService.getFileById(body.getString("fileId"));
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart("model", body.getString("model"));
            bodyBuilder.addFormDataPart("file", file.getOriginalFileName(), RequestBody.create(new File(file.getFilePath()), MediaType.get(FileUtil.getMimeType(file.getOriginalFileName()))));
            bodyBuilder.addFormDataPart("language", body.getString("language"));
            bodyBuilder.addFormDataPart("prompt", body.getString("prompt"));
            bodyBuilder.addFormDataPart("response_format", body.getString("response_format"));
            MultipartBody multipartBody = bodyBuilder.build();

            Request request = new Request.Builder() //
                    .url(baseUrl + OPENAI_AUDIO_TRANSCRIPTIONS) //
                    .post(multipartBody) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioTranscription openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 将音频转换为英语文本
     */
    @Override
    public void audioTranslation(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            RemoteSysFile file = remoteSystemService.getFileById(body.getString("fileId"));
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart("model", body.getString("model"));
            bodyBuilder.addFormDataPart("file", file.getOriginalFileName(), RequestBody.create(new File(file.getFilePath()), MediaType.get(FileUtil.getMimeType(file.getOriginalFileName()))));
            bodyBuilder.addFormDataPart("prompt", body.getString("prompt"));
            bodyBuilder.addFormDataPart("response_format", body.getString("response_format"));
            bodyBuilder.addFormDataPart("temperature", body.getString("temperature"));
            MultipartBody multipartBody = bodyBuilder.build();

            Request request = new Request.Builder() //
                    .url(baseUrl + OPENAI_AUDIO_TRANSLATIONS) //
                    .post(multipartBody) //
                    .addHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA) //
                    .addHeader(HttpHeaders.AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + apiKey) //
                    .build();

            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("audioTranslation openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 向量生成,输入文本的嵌入向量
     */
    @Override
    public void embedding(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + OPENAI_EMBEDDINGS;
            Request request = this.buildRequest(url, apiKey, body.toJSONString());
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("embedding openai ===================== exception:{}", e.getMessage());
        }
    }

    /**
     * 内容审核,对文本是否违反 OpenAI 的内容政策进行分类
     */
    @Override
    public void moderation(String baseUrl, String apiKey, JSONObject body, HttpServletResponse response) {
        try {
            String url = baseUrl + OPENAI_MODERATIONS;
            Request request = this.buildRequest(url, apiKey, body.toJSONString());
            response.setContentType(APPLICATION_JSON_VALUE);
            Response res = OK_HTTP_CLIENT.newCall(request).execute();
            dealResponse(res, response);
        } catch (Exception e) {
            log.error("moderation openai ===================== exception:{}", e.getMessage());
        }
    }

    public final RemoteSystemService remoteSystemService;

}
