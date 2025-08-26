package com.shuyoutech.api.service.api;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.constant.CommonConstants;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface ApiService {

    /**
     * 接口服务实现名称
     */
    String interfaceName();

    /**
     * 获取请求参数对象
     *
     * @param request 请求体
     * @return json
     */
    default JSONObject getBody(HttpServletRequest request) {
        String body = JakartaServletUtils.getBody(request);
        return JSONObject.parseObject(body).getJSONObject(CommonConstants.PARAMS);
    }

    /**
     * 执行方法
     *
     * @param request  请求参数
     * @param response 返回参数
     */
    void execute(HttpServletRequest request, HttpServletResponse response);

}
