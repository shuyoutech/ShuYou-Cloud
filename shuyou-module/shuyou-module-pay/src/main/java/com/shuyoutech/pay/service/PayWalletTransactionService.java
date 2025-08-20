package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayWalletTransactionBo;
import com.shuyoutech.pay.domain.entity.PayWalletTransactionEntity;
import com.shuyoutech.pay.domain.vo.PayWalletTransactionVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
public interface PayWalletTransactionService extends SuperService<PayWalletTransactionEntity, PayWalletTransactionVo> {

    Query buildQuery(PayWalletTransactionBo bo);

    PageResult<PayWalletTransactionVo> page(PageQuery<PayWalletTransactionBo> pageQuery);

    PayWalletTransactionVo detail(String id);

}
