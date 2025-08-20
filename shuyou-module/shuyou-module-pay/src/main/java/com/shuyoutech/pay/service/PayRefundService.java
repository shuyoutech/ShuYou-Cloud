package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayRefundBo;
import com.shuyoutech.pay.domain.entity.PayRefundEntity;
import com.shuyoutech.pay.domain.vo.PayRefundVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-08-20 19:57:14
 **/
public interface PayRefundService extends SuperService<PayRefundEntity, PayRefundVo> {

    Query buildQuery(PayRefundBo bo);

    PageResult<PayRefundVo> page(PageQuery<PayRefundBo> pageQuery);

    PayRefundVo detail(String id);

}
