package com.shuyoutech.pay.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.pay.domain.entity.PayWalletTransactionEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Data
@AutoMapper(target = PayWalletTransactionEntity.class)
@Schema(description = "会员钱包流水类")
public class PayWalletTransactionBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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
