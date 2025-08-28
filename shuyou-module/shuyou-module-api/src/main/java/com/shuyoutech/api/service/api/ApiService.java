package com.shuyoutech.api.service.api;

import com.alibaba.fastjson2.JSONObject;
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
     * 执行方法
     *
     * @param params  请求参数
     * @param response 返回参数
     */
    void execute(JSONObject params, HttpServletResponse response);

}
