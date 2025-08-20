package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayChannelBo;
import com.shuyoutech.pay.domain.entity.PayChannelEntity;
import com.shuyoutech.pay.domain.vo.PayChannelVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
public interface PayChannelService extends SuperService<PayChannelEntity, PayChannelVo> {

    Query buildQuery(PayChannelBo bo);

    PageResult<PayChannelVo> page(PageQuery<PayChannelBo> pageQuery);

    PayChannelVo detail(String id);

    String savePayChannel(PayChannelBo bo);

    boolean updatePayChannel(PayChannelBo bo);

    boolean deletePayChannel(List<String> ids);


}
