package com.shuyoutech.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.service.aigc.provider.OpenAiProvider;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.constant.AiConstants.EMBEDDING_MODEL_OPENAI_LIST;
import static com.shuyoutech.api.constant.AiConstants.MODEL;


/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    @Override
    public void embedding(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        if (EMBEDDING_MODEL_OPENAI_LIST.contains(bodyJson.getString(MODEL))) {

        }
    }

}
