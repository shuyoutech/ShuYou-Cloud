package com.shuyoutech.api.service.api;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.constant.AiConstants;
import com.shuyoutech.api.model.RemoteModel;
import com.shuyoutech.api.service.aigc.AigcService;
import com.shuyoutech.api.service.aigc.ApiModelFactory;
import com.shuyoutech.api.service.aigc.provider.ModelProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.enums.InterfaceFunctionEnum.AIGC_TEXT_TO_IMAGE;

/**
 * 图片生成
 *
 * @author YangChao
 * @date 2025-08-26 22:23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AigcTextToImageService implements ApiService {

    @Override
    public String interfaceName() {
        return AIGC_TEXT_TO_IMAGE.getValue();
    }

    @Override
    public void execute(JSONObject params, HttpServletResponse response) {
        RemoteModel model = aigcService.getModel(params.getString(AiConstants.MODEL));
        ModelProvider modelProvider = ApiModelFactory.getModelService(model.getProvider());
        modelProvider.textToImage(model.getBaseUrl(), model.getApiKey(), params, response);
    }

    private final AigcService aigcService;

}
