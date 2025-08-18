package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayOrderBo;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.shuyoutech.pay.domain.vo.PayOrderVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-23 11:04:39
 **/
public interface PayOrderService extends SuperService<PayOrderEntity, PayOrderVo> {

    Query buildQuery(PayOrderBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<PayOrderVo> page(PageQuery<PayOrderBo> pageQuery);

    PayOrderVo detail(String id);

    String savePayOrder(PayOrderBo bo);

    boolean updatePayOrder(PayOrderBo bo);

    boolean deletePayOrder(List<String> ids);

    boolean statusPayOrder(String id, String status);

}
