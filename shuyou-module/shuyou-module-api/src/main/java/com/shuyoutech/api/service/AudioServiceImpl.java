package com.shuyoutech.api.service;

import cn.hutool.core.net.multipart.MultipartFormData;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.constant.AiConstants.AUDIO_MODEL_OPENAI_LIST;
import static com.shuyoutech.api.constant.AiConstants.MODEL;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    @Override
    public void audioSpeech(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        if (AUDIO_MODEL_OPENAI_LIST.contains(bodyJson.getString(MODEL))) {

        }
    }

    @Override
    public void audioTranscription(HttpServletRequest request, HttpServletResponse response) {
        MultipartFormData multipart = JakartaServletUtils.getMultipart(request);
        if (AUDIO_MODEL_OPENAI_LIST.contains(multipart.getParam(MODEL))) {

        }
    }

    @Override
    public void audioTranslation(HttpServletRequest request, HttpServletResponse response) {
        MultipartFormData multipart = JakartaServletUtils.getMultipart(request);
        if (AUDIO_MODEL_OPENAI_LIST.contains(multipart.getParam(MODEL))) {

        }
    }
}
