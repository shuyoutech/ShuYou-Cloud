package com.shuyoutech.pay.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.pay.domain.vo.PayRechargePackageVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PayRechargePackageVo.class)
@Document(collection = "pay_recharge_package")
@Schema(description = "充值套餐表类")
public class PayRechargePackageEntity extends BaseEntity<PayRechargePackageEntity> {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "套餐金额")
    private Long packagePrice;

    @Schema(description = "赠送算力")
    private Long packageCredit;

}
