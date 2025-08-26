package com.shuyoutech.api.service.aigc;

import com.shuyoutech.api.model.RemoteModel;

/**
 * @author YangChao
 * @date 2025-08-26 22:29
 **/
public interface AigcService {

    RemoteModel getModel(String modelName);

}
