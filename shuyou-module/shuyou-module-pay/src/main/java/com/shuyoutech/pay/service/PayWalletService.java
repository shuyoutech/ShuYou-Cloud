package com.shuyoutech.pay.service;

import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayWalletBo;
import com.shuyoutech.pay.domain.entity.PayWalletEntity;
import com.shuyoutech.pay.domain.vo.PayWalletVo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
public interface PayWalletService extends SuperService<PayWalletEntity, PayWalletVo> {

    Query buildQuery(PayWalletBo bo);

    PageResult<PayWalletVo> page(PageQuery<PayWalletBo> pageQuery);

    PayWalletVo detail(String id);

    /**
     * 扣减钱包积分
     */
    void reduceWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, Long credit);

    /**
     * 增加钱包积分
     */
    void addWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, Long credit);

}
