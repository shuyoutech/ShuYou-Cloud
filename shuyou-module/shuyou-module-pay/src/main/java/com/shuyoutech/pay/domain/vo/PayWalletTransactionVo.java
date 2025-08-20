package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "会员钱包流水显示类")
public class PayWalletTransactionVo extends BaseVo {

    @Schema(description = "钱包ID")
    private String walletId;

    @Schema(description = "关联业务分类")
    private String payType;

    @Schema(description = "关联业务ID")
    private String payId;

    @Schema(description = "交易金额，单位分")
    private BigDecimal price;

    @Schema(description = " 交易后余额，单位分")
    private BigDecimal balance;

}
