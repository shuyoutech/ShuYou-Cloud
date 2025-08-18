package com.shuyoutech.member.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.member.domain.entity.MemberWalletEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author YangChao
 * @date 2025-08-04 23:48:45
 **/
@Data
@AutoMapper(target = MemberWalletEntity.class)
@Schema(description = "会员钱包类")
public class MemberWalletBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "余额，单位分")
    private BigDecimal balance;

    @Schema(description = "累计支出，单位分")
    private BigDecimal totalExpense;

    @Schema(description = "累计充值，单位分")
    private BigDecimal totalRecharge;

}
