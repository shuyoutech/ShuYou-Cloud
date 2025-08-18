package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钱包交易业务分类枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum WalletPayTypeEnum implements BaseEnum<String, String> {

    RECHARGE("recharge", "充值"),

    RECHARGE_REFUND("recharge_refund", "充值退款"),

    PAYMENT("payment", "支付"),

    PAYMENT_REFUND("payment_refund", "支付退款"),

    UPDATE_BALANCE("update_balance", "更新余额"),

    TRANSFER("transfer", "转账");

    private final String value;
    private final String label;

}
