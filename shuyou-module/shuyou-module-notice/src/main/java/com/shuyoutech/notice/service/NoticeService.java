package com.shuyoutech.notice.service;

import com.shuyoutech.notice.domain.bo.NoticeSendBo;

/**
 * @author YangChao
 * @date 2025-07-19 12:00
 **/
public interface NoticeService {

    void send(NoticeSendBo bo);

}
