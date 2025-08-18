package com.shuyoutech.pay.service;

import java.util.Map;

/**
 * @author YangChao
 * @date 2025-08-04 14:34
 **/
public interface PayNotifyService {

    void notifyOrder(String channelCode, Map<String, String> params, String body, Map<String, String> headers);

}
