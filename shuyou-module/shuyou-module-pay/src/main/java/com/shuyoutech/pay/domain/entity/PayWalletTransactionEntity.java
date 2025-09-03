package com.shuyoutech.pay.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.pay.domain.vo.PayWalletTransactionVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PayWalletTransactionVo.class)
@Document(collection = "pay_wallet_transaction")
@Schema(description = "会员钱包流水表类")
public class PayWalletTransactionEntity extends BaseEntity<PayWalletTransactionEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

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
