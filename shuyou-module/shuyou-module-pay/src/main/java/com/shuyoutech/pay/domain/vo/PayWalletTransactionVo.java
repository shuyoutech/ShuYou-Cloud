package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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

    @Schema(description = "充值积分，单位算力")
    private Long credit;

    @Schema(description = " 交易后积分，单位算力")
    private Long balance;

}
