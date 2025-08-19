package com.shuyoutech.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface AudioService {

    void audioSpeech(HttpServletRequest request, HttpServletResponse response);

    void audioTranscription(HttpServletRequest request, HttpServletResponse response);

    void audioTranslation(HttpServletRequest request, HttpServletResponse response);

}
