package com.shuyoutech.member.service;

import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.MemberWalletBo;
import com.shuyoutech.member.domain.entity.MemberWalletEntity;
import com.shuyoutech.member.domain.vo.MemberWalletVo;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
public interface MemberWalletService extends SuperService<MemberWalletEntity, MemberWalletVo> {

    Query buildQuery(MemberWalletBo bo);

    PageResult<MemberWalletVo> page(PageQuery<MemberWalletBo> pageQuery);

    MemberWalletVo detail(String id);

    /**
     * 扣减钱包余额
     */
    void reduceWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, BigDecimal price);

    /**
     * 增加钱包余额
     */
    void addWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, Integer price);

}
