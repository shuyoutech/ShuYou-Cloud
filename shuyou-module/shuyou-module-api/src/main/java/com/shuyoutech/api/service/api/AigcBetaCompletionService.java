package com.shuyoutech.api.service.api;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.constant.AiConstants;
import com.shuyoutech.api.model.RemoteModel;
import com.shuyoutech.api.service.aigc.ApiModelFactory;
import com.shuyoutech.api.service.aigc.AigcService;
import com.shuyoutech.api.service.aigc.provider.ModelProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-08-26 22:23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AigcBetaCompletionService implements ApiService {

    @Override
    public String interfaceName() {
        return "aigcBetaCompletion";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        JSONObject body = getBody(request);
        RemoteModel model = aigcService.getModel(body.getString(AiConstants.MODEL));
        ModelProvider modelProvider = ApiModelFactory.getModelService(model.getProvider());
        modelProvider.betaCompletion(model.getBaseUrl(), model.getApiKey(), body, response);
    }

    private final AigcService aigcService;

}
