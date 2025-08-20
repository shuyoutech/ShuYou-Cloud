package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayRefundBo;
import com.shuyoutech.pay.domain.entity.PayRefundEntity;
import com.shuyoutech.pay.domain.vo.PayRefundVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-20 19:57:14
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundServiceImpl extends SuperServiceImpl<PayRefundEntity, PayRefundVo> implements PayRefundService {

    @Override
    public List<PayRefundVo> convertTo(List<PayRefundEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayRefundVo convertTo(PayRefundEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayRefundBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getCreateUserId())) {
            query.addCriteria(Criteria.where("createUserId").is(bo.getCreateUserId()));
        }
        return query;
    }

    @Override
    public PageResult<PayRefundVo> page(PageQuery<PayRefundBo> pageQuery) {
        PayRefundBo query = pageQuery.getQuery();
        query.setCreateUserId(AuthUtils.getLoginUserId());
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(query));
        return this.selectPageVo(page);
    }

    @Override
    public PayRefundVo detail(String id) {
        PayRefundEntity entity = this.getById(id);
        return convertTo(entity);
    }

}