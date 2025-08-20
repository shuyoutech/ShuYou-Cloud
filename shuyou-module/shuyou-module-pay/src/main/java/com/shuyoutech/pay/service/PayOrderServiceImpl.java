package com.shuyoutech.pay.service;

import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayOrderBo;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.shuyoutech.pay.domain.vo.PayOrderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-23 11:04:39
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends SuperServiceImpl<PayOrderEntity, PayOrderVo> implements PayOrderService {

    @Override
    public List<PayOrderVo> convertTo(List<PayOrderEntity> list) {
        List<PayOrderVo> result = CollectionUtils.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        list.forEach(e -> {
            PayOrderVo vo = MapstructUtils.convert(e, this.voClass);
            vo.setStatusName(EnumUtils.getLabelByValue(PayOrderStatusEnum.class, e.getStatus()));
            vo.setPayPriceStr(FormatUtils.formatDotZero(NumberUtils.div(e.getPayPrice().toString(), "100", 2).toPlainString()));
            result.add(vo);
        });
        return result;
    }

    public PayOrderVo convertTo(PayOrderEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayOrderBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getCreateUserId())) {
            query.addCriteria(Criteria.where("createUserId").is(bo.getCreateUserId()));
        }
        return query;
    }

    @Override
    public PageResult<PayOrderVo> page(PageQuery<PayOrderBo> pageQuery) {
        PayOrderBo query = pageQuery.getQuery();
        query.setCreateUserId(AuthUtils.getLoginUserId());
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(query));
        return this.selectPageVo(page);
    }

    @Override
    public PayOrderVo detail(String id) {
        PayOrderEntity entity = this.getById(id);
        return convertTo(entity);
    }

}