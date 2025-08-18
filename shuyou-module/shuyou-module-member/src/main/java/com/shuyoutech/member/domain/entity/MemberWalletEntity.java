package com.shuyoutech.member.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.member.domain.vo.MemberWalletVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MemberWalletVo.class)
@Document(collection = "member_wallet")
@Schema(description = "会员钱包表类")
public class MemberWalletEntity extends BaseEntity<MemberWalletEntity> {

    @Schema(description = "余额，单位分")
    @Field(targetType = DECIMAL128)
    private BigDecimal balance;

    @Schema(description = "累计支出，单位分")
    @Field(targetType = DECIMAL128)
    private BigDecimal totalExpense;

    @Schema(description = "累计充值，单位分")
    private BigDecimal totalRecharge;

}
