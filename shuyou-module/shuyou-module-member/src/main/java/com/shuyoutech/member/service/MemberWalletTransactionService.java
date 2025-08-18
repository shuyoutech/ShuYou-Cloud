package com.shuyoutech.member.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.MemberWalletTransactionBo;
import com.shuyoutech.member.domain.entity.MemberWalletTransactionEntity;
import com.shuyoutech.member.domain.vo.MemberWalletTransactionVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
public interface MemberWalletTransactionService extends SuperService<MemberWalletTransactionEntity, MemberWalletTransactionVo> {

    Query buildQuery(MemberWalletTransactionBo bo);

    PageResult<MemberWalletTransactionVo> page(PageQuery<MemberWalletTransactionBo> pageQuery);

    MemberWalletTransactionVo detail(String id);

}
