package com.shuyoutech.member.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.member.domain.vo.MemberWalletTransactionVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-08-05 10:13:30
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MemberWalletTransactionVo.class)
@Document(collection = "member_wallet_transaction")
@Schema(description = "会员钱包流水表类")
public class MemberWalletTransactionEntity extends BaseEntity<MemberWalletTransactionEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

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
