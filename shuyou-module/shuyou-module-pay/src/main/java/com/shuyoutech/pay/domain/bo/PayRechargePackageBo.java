package com.shuyoutech.pay.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.pay.domain.entity.PayRechargePackageEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
@Data
@AutoMapper(target = PayRechargePackageEntity.class)
@Schema(description = "充值套餐类")
public class PayRechargePackageBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "套餐金额")
    private Long packagePrice;

    @Schema(description = "赠送算力")
    private Long packageCredit;

}
