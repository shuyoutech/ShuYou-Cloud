package com.shuyoutech.api.service.aigc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface ApiAigcService {

    void chatCompletion(HttpServletRequest request, HttpServletResponse response);

    void betaCompletion(HttpServletRequest request, HttpServletResponse response);

    void embedding(HttpServletRequest request, HttpServletResponse response);

}
