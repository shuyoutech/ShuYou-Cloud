package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "会员钱包显示类")
public class PayWalletVo extends BaseVo {

    @Schema(description = "积分余额，单位算力")
    private Long balance;

    @Schema(description = "积分累计支出，单位算力")
    private Long totalExpense;

    @Schema(description = "积分累计充值，单位算力")
    private Long totalRecharge;

}
