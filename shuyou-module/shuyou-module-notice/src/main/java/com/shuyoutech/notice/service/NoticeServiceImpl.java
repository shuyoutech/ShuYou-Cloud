package com.shuyoutech.notice.service;

import com.shuyoutech.common.disruptor.model.DisruptorData;
import com.shuyoutech.notice.domain.bo.NoticeSendBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shuyoutech.common.disruptor.init.DisruptorRunner.disruptorProducer;

/**
 * @author YangChao
 * @date 2025-07-19 12:00
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    public static final String NOTICE_SEND_SERVICE = "noticeSendServiceImpl";

    @Override
    public void send(NoticeSendBo bo) {
        DisruptorData disruptorData = new DisruptorData();
        disruptorData.setServiceName(NOTICE_SEND_SERVICE);
        disruptorData.setData(bo);
        disruptorProducer.pushData(disruptorData);
    }

}
