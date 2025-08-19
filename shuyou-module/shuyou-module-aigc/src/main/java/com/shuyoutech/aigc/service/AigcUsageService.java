package com.shuyoutech.aigc.service;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
public interface AigcUsageService {

   List<JSONObject> usage(String date);

}
