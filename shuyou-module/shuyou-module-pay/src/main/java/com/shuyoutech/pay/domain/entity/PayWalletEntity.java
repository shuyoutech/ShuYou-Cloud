package com.shuyoutech.pay.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.pay.domain.vo.PayWalletVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PayWalletVo.class)
@Document(collection = "pay_wallet")
@Schema(description = "会员钱包表类")
public class PayWalletEntity extends BaseEntity<PayWalletEntity> {

    @Schema(description = "用户类型")
    private String userType;

    /**
     * 1R == 200算力扣除
     */
    @Schema(description = "积分余额，单位算力")
    private Long balance;

    @Schema(description = "积分累计支出，单位算力")
    private Long totalExpense;

    @Schema(description = "积分累计充值，单位算力")
    private Long totalRecharge;

}
