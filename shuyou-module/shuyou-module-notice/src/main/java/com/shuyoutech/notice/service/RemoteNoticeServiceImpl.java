package com.shuyoutech.notice.service;

import cn.hutool.core.util.RandomUtil;
import com.shuyoutech.api.service.RemoteNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-08-14 09:45
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteNoticeServiceImpl implements RemoteNoticeService {

    @Override
    public void sendSms(String templateCode, String mobile) {
        String code = RandomUtil.randomNumbers(4);
        noticeSmsService.sendSms(mobile, code, templateCode);
    }

    private final NoticeSmsService noticeSmsService;
}
