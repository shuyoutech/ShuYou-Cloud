package com.shuyoutech.member.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.member.domain.entity.MemberWalletTransactionEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Data
@AutoMapper(target = MemberWalletTransactionEntity.class)
@Schema(description = "会员钱包流水类")
public class MemberWalletTransactionBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "钱包ID")
    private String walletId;

    @Schema(description = "关联业务分类")
    private String payType;

    @Schema(description = "关联业务ID")
    private String payId;

    @Schema(description = "交易金额，单位分")
    @Field(targetType = DECIMAL128)
    private BigDecimal price;

    @Schema(description = " 交易后余额，单位分")
    @Field(targetType = DECIMAL128)
    private BigDecimal balance;

}
