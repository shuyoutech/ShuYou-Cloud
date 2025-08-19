package com.shuyoutech.member.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "会员钱包显示类")
public class MemberWalletVo extends BaseVo {

    @Schema(description = "余额，单位分")
    private BigDecimal balance;

    @Schema(description = "累计支出，单位分")
    private BigDecimal totalExpense;

    @Schema(description = "累计充值，单位分")
    private BigDecimal totalRecharge;

    @Schema(description = "余额，单位元")
    private String balanceStr;

    @Schema(description = "累计支出，单位元")
    private String totalExpenseStr;

    @Schema(description = "累计充值，单位元")
    private String totalRechargeStr;

}
