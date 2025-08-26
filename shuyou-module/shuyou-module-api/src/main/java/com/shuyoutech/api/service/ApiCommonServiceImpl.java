package com.shuyoutech.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-08-22 14:21
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommonServiceImpl implements ApiCommonService {

    @Override
    public void commonInterfaceV1(HttpServletRequest request, HttpServletResponse response) {
        String body = JakartaServletUtils.getBody(request);
        JSONObject bodyJson = JSONObject.parseObject(body);
        String api = bodyJson.getString("api");

    }

}
