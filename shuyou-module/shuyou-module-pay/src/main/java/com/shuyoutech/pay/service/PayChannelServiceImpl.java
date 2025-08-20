package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayChannelBo;
import com.shuyoutech.pay.domain.entity.PayChannelEntity;
import com.shuyoutech.pay.domain.vo.PayChannelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelServiceImpl extends SuperServiceImpl<PayChannelEntity, PayChannelVo> implements PayChannelService {

    @Override
    public List<PayChannelVo> convertTo(List<PayChannelEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayChannelVo convertTo(PayChannelEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayChannelBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<PayChannelVo> page(PageQuery<PayChannelBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayChannelVo detail(String id) {
        PayChannelEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String savePayChannel(PayChannelBo bo) {
        PayChannelEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updatePayChannel(PayChannelBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deletePayChannel(List<String> ids) {
        return this.deleteByIds(ids);
    }

}