package com.shuyoutech.pay.service;

import cn.hutool.core.util.IdUtil;
import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.NumberUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedissonUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.pay.domain.entity.PayWalletTransactionEntity;
import com.shuyoutech.pay.domain.bo.PayWalletBo;
import com.shuyoutech.pay.domain.entity.PayWalletEntity;
import com.shuyoutech.pay.domain.vo.PayWalletVo;
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
public class PayWalletServiceImpl extends SuperServiceImpl<PayWalletEntity, PayWalletVo> implements PayWalletService {

    @Override
    public List<PayWalletVo> convertTo(List<PayWalletEntity> list) {
        List<PayWalletVo> result = CollectionUtils.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        list.forEach(e -> {
            PayWalletVo vo = MapstructUtils.convert(e, PayWalletVo.class);
            vo.setBalanceStr(NumberUtils.div(e.getBalance(), 100, 2).toPlainString());
            vo.setTotalExpenseStr(NumberUtils.div(e.getTotalExpense(), 100, 2).toPlainString());
            vo.setTotalRechargeStr(NumberUtils.div(e.getTotalRecharge(), 100, 2).toPlainString());
            result.add(vo);
        });
        return result;
    }

    public PayWalletVo convertTo(PayWalletEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(PayWalletBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<PayWalletVo> page(PageQuery<PayWalletBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public PayWalletVo detail(String id) {
        PayWalletEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public void reduceWalletBalance(String walletId, WalletPayTypeEnum payType, String payId, BigDecimal price) {
        RedissonUtils.lock(walletId, 120000L, () -> {
            // 1. 获取钱包
            PayWalletEntity wallet = MongoUtils.getById(walletId, PayWalletEntity.class);
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
                    MongoUtils.patch(walletId, update, PayWalletEntity.class);
                    break;
                }
                case RECHARGE_REFUND: {
                    Update update = new Update();
                    update.set("balance", balance.subtract(price));
                    update.set("totalRecharge", totalRecharge.subtract(price));
                    MongoUtils.patch(walletId, update, PayWalletEntity.class);
                    break;
                }
                default: {
                    log.error("reduceWalletBalance ======== 支付方式:{}不支持", payType.getValue());
                }
            }
            PayWalletTransactionEntity transaction = new PayWalletTransactionEntity();
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
            PayWalletEntity wallet = MongoUtils.getById(walletId, PayWalletEntity.class);
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
                    MongoUtils.patch(walletId, update, PayWalletEntity.class);
                    break;
                }
                case RECHARGE: {
                    Update update = new Update();
                    update.set("balance", balance.add(BigDecimal.valueOf(price)));
                    update.set("totalRecharge", totalRecharge.add(BigDecimal.valueOf(price)));
                    MongoUtils.patch(walletId, update, PayWalletEntity.class);
                    break;
                }
                case UPDATE_BALANCE:
                case TRANSFER:
                    Update update = new Update();
                    update.set("balance", balance.add(BigDecimal.valueOf(price)));
                    MongoUtils.patch(walletId, update, PayWalletEntity.class);
                    break;
                default: {
                    log.error("addWalletBalance ======== 支付方式:{}不支持", payType.getValue());
                }
            }
            PayWalletTransactionEntity transaction = new PayWalletTransactionEntity();
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