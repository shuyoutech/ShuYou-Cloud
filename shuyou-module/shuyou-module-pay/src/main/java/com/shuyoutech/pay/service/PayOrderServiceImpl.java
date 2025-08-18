package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayOrderBo;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.shuyoutech.pay.domain.vo.PayOrderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayOrderVo convertTo(PayOrderEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayOrderBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        PayOrderEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<PayOrderVo> page(PageQuery<PayOrderBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayOrderVo detail(String id) {
        PayOrderEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String savePayOrder(PayOrderBo bo) {
        PayOrderEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updatePayOrder(PayOrderBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deletePayOrder(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public boolean statusPayOrder(String id, String status) {
        PayOrderEntity entity = this.getById(id);
        if (null == entity) {
            return false;
        }
        Update update = new Update();
        update.set("status", status);
        return MongoUtils.patch(id, update, PayOrderEntity.class);
    }

}