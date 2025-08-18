package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayNotifyRecordBo;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import com.shuyoutech.pay.domain.vo.PayNotifyRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-23 16:26:15
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayNotifyRecordServiceImpl extends SuperServiceImpl<PayNotifyRecordEntity, PayNotifyRecordVo> implements PayNotifyRecordService {

    @Override
    public List<PayNotifyRecordVo> convertTo(List<PayNotifyRecordEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayNotifyRecordVo convertTo(PayNotifyRecordEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayNotifyRecordBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<PayNotifyRecordVo> page(PageQuery<PayNotifyRecordBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayNotifyRecordVo detail(String id) {
        PayNotifyRecordEntity entity = this.getById(id);
        return convertTo(entity);
    }

}