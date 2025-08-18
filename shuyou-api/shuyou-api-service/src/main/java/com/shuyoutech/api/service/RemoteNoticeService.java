package com.shuyoutech.api.service;

/**
 * @author YangChao
 * @date 2025-05-15 13:55
 **/
public interface RemoteNoticeService {

    void sendSms(String templateCode, String mobile);

}
