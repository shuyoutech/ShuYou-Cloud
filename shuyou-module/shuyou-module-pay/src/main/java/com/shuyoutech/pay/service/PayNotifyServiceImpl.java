package com.shuyoutech.pay.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.shuyoutech.api.enums.PayChannelEnum;
import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.api.enums.WalletPayTypeEnum;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.member.service.MemberWalletService;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import com.shuyoutech.pay.domain.entity.PayOrderEntity;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static com.shuyoutech.pay.init.PayRunner.notificationParser;

/**
 * @author YangChao
 * @date 2025-08-04 14:35
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayNotifyServiceImpl implements PayNotifyService {

    @Async
    @Override
    public void notifyOrder(String channelCode, Map<String, String> params, String body, Map<String, String> headers) {
        try {
            // 接收到之后插入商户通知记录表中
            PayNotifyRecordEntity record = new PayNotifyRecordEntity();
            record.setId(IdUtil.fastSimpleUUID());
            record.setCreateTime(new Date());
            record.setChannelCode(channelCode);
            record.setRequestHeaders(JSON.toJSONString(headers));
            record.setRequestParams(JSON.toJSONString(params));
            record.setRequestBody(body);
            MongoUtils.save(record);

            // 解析请求体
            if (PayChannelEnum.WEIXIN_NATIVE.getValue().equals(channelCode)) {
                // 构造 RequestParam
                RequestParam requestParam = new RequestParam.Builder() //
                        .serialNumber(headers.get("wechatpay-serial")) //
                        .nonce(headers.get("wechatpay-nonce")) //
                        .signature(headers.get("wechatpay-signature")) //
                        .timestamp(headers.get("wechatpay-timestamp")) //
                        .body(body) //
                        .build();
                // 以支付通知回调为例，验签、解密并转换成 Transaction
                Transaction transaction = notificationParser.parse(requestParam, Transaction.class);
                if (null == transaction) {
                    return;
                }
                String outTradeNo = transaction.getOutTradeNo();
                PayOrderEntity payOrder = payOrderService.getById(outTradeNo);
                if (null == payOrder) {
                    log.error("notifyOrder ======================= outTradeNo：{} 不存在!", outTradeNo);
                    return;
                }
                Update update = new Update();
                if (Transaction.TradeStateEnum.SUCCESS == transaction.getTradeState()) {
                    update.set("status", PayOrderStatusEnum.SUCCESS.getValue());
                    update.set("successTime", DateUtil.parse(transaction.getSuccessTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN).toJdkDate());
                } else {
                    update.set("status", PayOrderStatusEnum.FAILED.getValue());
                }
                update.set("mchOrderNo", transaction.getTransactionId());
                MongoUtils.patch(outTradeNo, update, PayOrderEntity.class);

                Update update2 = new Update();
                update2.set("status", "2");
                update2.set("tradeType", transaction.getTradeType());
                update2.set("orderId", outTradeNo);
                update2.set("mchId", transaction.getSpMchid());
                update2.set("appId", transaction.getSpAppid());
                update2.set("mchOrderNo", transaction.getTransactionId());
                update2.set("amount", transaction.getAmount().getTotal());
                MongoUtils.patch(record.getId(), update2, PayNotifyRecordEntity.class);

                String userId = payOrder.getCreateUserId();
                memberWalletService.addWalletBalance(userId, WalletPayTypeEnum.RECHARGE, outTradeNo, transaction.getAmount().getTotal());
            } else {
                log.info("notifyOrder ======================= channelCode{}", channelCode);
            }
        } catch (Exception e) {
            log.error("notifyOrder ================== exception:{}", e.getMessage());
        }

    }


    private final PayOrderService payOrderService;
    private final MemberWalletService memberWalletService;
}
