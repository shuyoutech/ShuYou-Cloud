package com.shuyoutech.pay.service;

import com.shuyoutech.api.service.RemotePayService;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.pay.domain.entity.PayWalletEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-08-20 16:11
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RemotePayServiceImpl implements RemotePayService {

    @Override
    public void initPayWallet(String userType, String userId) {
        PayWalletEntity wallet = new PayWalletEntity();
        wallet.setId(userId);
        wallet.setUserType(userType);
        wallet.setBalance(500L);
        wallet.setTotalExpense(0L);
        wallet.setTotalRecharge(0L);
        MongoUtils.save(wallet);
    }

}
