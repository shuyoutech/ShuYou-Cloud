package com.shuyoutech.api.service.aigc;

import com.shuyoutech.api.model.RemoteModel;
import com.shuyoutech.api.service.RemoteAigcService;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-07-13 15:50
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiAigcServiceImpl implements AigcService {

    @Override
    public RemoteModel getModel(String modelName) {
        RemoteModel model = remoteAigcService.getModel(modelName);
        if (null == model) {
            throw new BusinessException(StringUtils.format("model:{} not found", modelName));
        }
        return model;
    }

    private final RemoteAigcService remoteAigcService;

}
