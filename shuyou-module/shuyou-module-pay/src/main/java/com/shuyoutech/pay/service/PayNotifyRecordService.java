package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayNotifyRecordBo;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import com.shuyoutech.pay.domain.vo.PayNotifyRecordVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-07-23 16:26:15
 **/
public interface PayNotifyRecordService extends SuperService<PayNotifyRecordEntity, PayNotifyRecordVo> {

    Query buildQuery(PayNotifyRecordBo bo);

    PageResult<PayNotifyRecordVo> page(PageQuery<PayNotifyRecordBo> pageQuery);

    PayNotifyRecordVo detail(String id);

}
