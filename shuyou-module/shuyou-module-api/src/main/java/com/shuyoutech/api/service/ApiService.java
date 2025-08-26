package com.shuyoutech.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface ApiService {

    Pair<String, String> getApiKey(String modelName);

    void chatCompletion(HttpServletRequest request, HttpServletResponse response);

    void betaCompletion(HttpServletRequest request, HttpServletResponse response);

    void embedding(HttpServletRequest request, HttpServletResponse response);

    void multimodalEmbedding(HttpServletRequest request, HttpServletResponse response);

}
