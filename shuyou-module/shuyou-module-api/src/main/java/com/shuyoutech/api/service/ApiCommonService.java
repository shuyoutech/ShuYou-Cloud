package com.shuyoutech.api.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author YangChao
 * @date 2025-08-22 14:21
 **/
public interface ApiCommonService {

    void interfaceV1(JSONObject request, HttpServletResponse response);

}
