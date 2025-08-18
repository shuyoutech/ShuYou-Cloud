package com.shuyoutech.member.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.member.domain.bo.MemberWalletTransactionBo;
import com.shuyoutech.member.domain.entity.MemberWalletTransactionEntity;
import com.shuyoutech.member.domain.vo.MemberWalletTransactionVo;
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
public class MemberWalletTransactionServiceImpl extends SuperServiceImpl<MemberWalletTransactionEntity, MemberWalletTransactionVo> implements MemberWalletTransactionService {

    @Override
    public List<MemberWalletTransactionVo> convertTo(List<MemberWalletTransactionEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public MemberWalletTransactionVo convertTo(MemberWalletTransactionEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(MemberWalletTransactionBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<MemberWalletTransactionVo> page(PageQuery<MemberWalletTransactionBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public MemberWalletTransactionVo detail(String id) {
        MemberWalletTransactionEntity entity = this.getById(id);
        return convertTo(entity);
    }

}