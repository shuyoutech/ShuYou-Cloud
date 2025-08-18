package com.shuyoutech.member.service;

import cn.hutool.core.util.IdUtil;
import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedissonUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.member.domain.bo.MemberWalletBo;
import com.shuyoutech.member.domain.entity.MemberWalletEntity;
import com.shuyoutech.member.domain.entity.MemberWalletTransactionEntity;
import com.shuyoutech.member.domain.vo.MemberWalletVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberWalletServiceImpl extends SuperServiceImpl<MemberWalletEntity, MemberWalletVo> implements MemberWalletService {

    @Override
    public List<MemberWalletVo> convertTo(List<MemberWalletEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public MemberWalletVo convertTo(MemberWalletEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(MemberWalletBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<MemberWalletVo> page(PageQuery<MemberWalletBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public MemberWalletVo detail(String id) {
        MemberWalletEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public void reduceWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, BigDecimal price) {
        RedissonUtils.lock(walletId, 120000L, () -> {
            // 1. 获取钱包
            MemberWalletEntity wallet = MongoUtils.getById(walletId, MemberWalletEntity.class);
            if (null == wallet) {
                log.error("reduceWalletBalance ======== 用户钱包:{}不存在", walletId);
                return null;
            }
            BigDecimal balance = wallet.getBalance();
            BigDecimal totalExpense = wallet.getTotalExpense();
            BigDecimal totalRecharge = wallet.getTotalRecharge();
            switch (payType) {
                case PAYMENT: {
                    Update update = new Update();
                    update.set("balance", balance.subtract(price));
                    update.set("totalExpense", totalExpense.add(price));
                    MongoUtils.patch(walletId, update, MemberWalletEntity.class);
                    break;
                }
                case RECHARGE_REFUND: {
                    Update update = new Update();
                    update.set("balance", balance.subtract(price));
                    update.set("totalRecharge", totalRecharge.subtract(price));
                    MongoUtils.patch(walletId, update, MemberWalletEntity.class);
                    break;
                }
                default: {
                    log.error("reduceWalletBalance ======== 支付方式:{}不支持", payType.getValue());
                }
            }
            MemberWalletTransactionEntity transaction = new MemberWalletTransactionEntity();
            transaction.setId(IdUtil.fastSimpleUUID());
            transaction.setCreateTime(new Date());
            transaction.setWalletId(walletId);
            transaction.setPayType(payType.getValue());
            transaction.setPayId(payId);
            transaction.setPrice(price);
            transaction.setBalance(balance.subtract(price));
            return MongoUtils.save(transaction);
        });
    }

    @Override
    public void addWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, Integer price) {
        RedissonUtils.lock(walletId, 120000L, () -> {
            // 1. 获取钱包
            MemberWalletEntity wallet = MongoUtils.getById(walletId, MemberWalletEntity.class);
            if (null == wallet) {
                log.error("addWalletBalance ======== 用户钱包:{}不存在", walletId);
                return null;
            }
            BigDecimal balance = wallet.getBalance();
            BigDecimal totalExpense = wallet.getTotalExpense();
            BigDecimal totalRecharge = wallet.getTotalRecharge();
            switch (payType) {
                case PAYMENT_REFUND: {
                    Update update = new Update();
                    update.set("balance", balance.add(BigDecimal.valueOf(price)));
                    update.set("totalExpense", totalExpense.subtract(BigDecimal.valueOf(price)));
                    MongoUtils.patch(walletId, update, MemberWalletEntity.class);
                    break;
                }
                case RECHARGE: {
                    Update update = new Update();
                    update.set("balance", balance.add(BigDecimal.valueOf(price)));
                    update.set("totalRecharge", totalRecharge.add(BigDecimal.valueOf(price)));
                    MongoUtils.patch(walletId, update, MemberWalletEntity.class);
                    break;
                }
                case UPDATE_BALANCE:
                case TRANSFER:
                    Update update = new Update();
                    update.set("balance", balance.add(BigDecimal.valueOf(price)));
                    MongoUtils.patch(walletId, update, MemberWalletEntity.class);
                    break;
                default: {
                    log.error("addWalletBalance ======== 支付方式:{}不支持", payType.getValue());
                }
            }
            MemberWalletTransactionEntity transaction = new MemberWalletTransactionEntity();
            transaction.setId(IdUtil.fastSimpleUUID());
            transaction.setCreateTime(new Date());
            transaction.setWalletId(walletId);
            transaction.setPayType(payType.getValue());
            transaction.setPayId(payId);
            transaction.setPrice(BigDecimal.valueOf(price));
            transaction.setBalance(balance.add(BigDecimal.valueOf(price)));
            return MongoUtils.save(transaction);
        });
    }

}