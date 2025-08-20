package com.shuyoutech.pay.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.bo.PayWalletTransactionBo;
import com.shuyoutech.pay.domain.entity.PayWalletTransactionEntity;
import com.shuyoutech.pay.domain.vo.PayWalletTransactionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayWalletTransactionServiceImpl extends SuperServiceImpl<PayWalletTransactionEntity, PayWalletTransactionVo> implements PayWalletTransactionService {

    @Override
    public List<PayWalletTransactionVo> convertTo(List<PayWalletTransactionEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public PayWalletTransactionVo convertTo(PayWalletTransactionEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayWalletTransactionBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<PayWalletTransactionVo> page(PageQuery<PayWalletTransactionBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayWalletTransactionVo detail(String id) {
        PayWalletTransactionEntity entity = this.getById(id);
        return convertTo(entity);
    }

}