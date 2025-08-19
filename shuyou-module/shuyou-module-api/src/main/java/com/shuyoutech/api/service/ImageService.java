package com.shuyoutech.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface ImageService {

    void imageGeneration(HttpServletRequest request, HttpServletResponse response);

}
