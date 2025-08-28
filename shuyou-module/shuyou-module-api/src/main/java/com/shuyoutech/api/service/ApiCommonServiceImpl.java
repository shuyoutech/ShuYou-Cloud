package com.shuyoutech.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.service.api.ApiService;
import com.shuyoutech.common.core.constant.CommonConstants;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.api.init.ApiRunner.serviceMap;

/**
 * @author YangChao
 * @date 2025-08-22 14:21
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommonServiceImpl implements ApiCommonService {

    private ApiService getService(String interfaceName) {
        ApiService apiService = serviceMap.get(interfaceName);
        if (null == apiService) {
            throw new BusinessException(StringUtils.format("interfaceName:{} is not exist", interfaceName));
        }
        return apiService;
    }

    @Override
    public void interfaceV1(JSONObject request, HttpServletResponse response) {
        String api = request.getString(CommonConstants.API);
        JSONObject params = request.getJSONObject(CommonConstants.PARAMS);
        ApiService apiService = getService(api);
        apiService.execute(params, response);
    }

}
