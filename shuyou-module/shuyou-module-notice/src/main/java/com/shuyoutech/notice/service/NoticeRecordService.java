package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.notice.domain.bo.NoticeRecordBo;
import com.shuyoutech.notice.domain.entity.NoticeRecordEntity;
import com.shuyoutech.notice.domain.vo.NoticeRecordVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
public interface NoticeRecordService extends SuperService<NoticeRecordEntity, NoticeRecordVo> {

    Query buildQuery(NoticeRecordBo bo);

    PageResult<NoticeRecordVo> page(PageQuery<NoticeRecordBo> pageQuery);

    NoticeRecordVo detail(String id);

}
