package com.shuyoutech.api.service.aigc;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.constant.AiConstants;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.constant.AiConstants.*;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AigcServiceImpl implements AigcService {

    @Override
    public void chatCompletion(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String model = bodyJson.getString(AiConstants.MODEL);
        if (CHAT_MODEL_ALIYUN_LIST.contains(model)) {

        } else if (CHAT_MODEL_DEEPSEEK_LIST.contains(model)) {

        } else if (CHAT_MODEL_OPENAI_LIST.contains(model)) {

        }
    }

    @Override
    public void betaCompletion(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String model = bodyJson.getString(MODEL);
        if (CHAT_MODEL_DEEPSEEK_LIST.contains(model)) {

        }
    }

}
