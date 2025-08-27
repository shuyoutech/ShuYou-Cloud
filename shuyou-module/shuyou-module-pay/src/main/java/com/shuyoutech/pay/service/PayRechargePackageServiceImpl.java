package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayRechargePackageBo;
import com.shuyoutech.pay.domain.entity.PayRechargePackageEntity;
import com.shuyoutech.pay.domain.vo.PayRechargePackageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRechargePackageServiceImpl extends SuperServiceImpl<PayRechargePackageEntity, PayRechargePackageVo> implements PayRechargePackageService {

    @Override
    public List<PayRechargePackageVo> convertTo(List<PayRechargePackageEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayRechargePackageVo convertTo(PayRechargePackageEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayRechargePackageBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<PayRechargePackageVo> page(PageQuery<PayRechargePackageBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayRechargePackageVo detail(String id) {
        PayRechargePackageEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String savePayRechargePackage(PayRechargePackageBo bo) {
        PayRechargePackageEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updatePayRechargePackage(PayRechargePackageBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deletePayRechargePackage(List<String> ids) {
        return this.deleteByIds(ids);
    }

}