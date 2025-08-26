package com.shuyoutech.api.service.aigc;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.constant.AiConstants;
import com.shuyoutech.api.enums.AiProviderTypeEnum;
import com.shuyoutech.api.model.RemoteModel;
import com.shuyoutech.api.service.ApiService;
import com.shuyoutech.api.service.RemoteAigcService;
import com.shuyoutech.api.service.aigc.provider.*;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.constant.AiConstants.*;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiAigcServiceImpl implements ApiService {

    private RemoteModel getModel(String modelName) {
        RemoteModel model = remoteAigcService.getModel(modelName);
        if (null == model) {
            log.error("getModel =============== model:{} not found", modelName);
            return null;
        }
        return model;
    }

    @Override
    public Pair<String, String> getApiKey(String modelName) {
        return null;
    }

    @Override
    public void chatCompletion(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String modelName = bodyJson.getString(AiConstants.MODEL);
        RemoteModel model = getModel(modelName);
        if (null == model) {
            return;
        }
        String provider = model.getProvider();
        String baseUrl = model.getBaseUrl();
        String apiKey = model.getApiKey();
        ModelProvider modelProvider = AigcModelFactory.getModelService(provider);
        modelProvider.chatCompletion(baseUrl, apiKey, body, response);
    }

    @Override
    public void betaCompletion(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String model = bodyJson.getString(MODEL);
        if (CHAT_MODEL_DEEPSEEK_LIST.contains(model)) {

        }
    }

    @Override
    public void embedding(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String modelName = bodyJson.getString(MODEL);
        if (EMBEDDING_MODEL_OPENAI_LIST.contains(modelName)) {
            String providerName = AiProviderTypeEnum.OPENAI.getValue();
            RemoteModel model = remoteAigcService.getModel(providerName, modelName);
            if (null == model) {
                log.error("embedding =============== openai model:{} not found", modelName);
                return;
            }
            OpenAiProvider provider = new OpenAiProvider(model.getBaseUrl(), model.getApiKey());
            provider.embedding(body, response);
        } else if (EMBEDDING_MODEL_ALIYUN_LIST.contains(modelName)) {
            String providerName = AiProviderTypeEnum.ALIYUN.getValue();
            RemoteModel model = remoteAigcService.getModel(providerName, modelName);
            if (null == model) {
                log.error("embedding =============== aliyun model:{} not found", modelName);
                return;
            }
            AliyunProvider provider = new AliyunProvider(model.getBaseUrl(), model.getApiKey());
            provider.embedding(body, response);
        }
    }

    @Override
    public void multimodalEmbedding(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String modelName = bodyJson.getString(MODEL);
        if (EMBEDDING_MODEL_ALIYUN_LIST.contains(modelName)) {
            String providerName = AiProviderTypeEnum.ALIYUN.getValue();
            RemoteModel model = remoteAigcService.getModel(providerName, modelName);
            if (null == model) {
                log.error("multimodalEmbedding =============== aliyun model:{} not found", modelName);
                return;
            }
            AliyunProvider provider = new AliyunProvider(model.getBaseUrl(), model.getApiKey());
            provider.multimodalEmbedding(body, response);
        }
    }

    private final RemoteAigcService remoteAigcService;

}
