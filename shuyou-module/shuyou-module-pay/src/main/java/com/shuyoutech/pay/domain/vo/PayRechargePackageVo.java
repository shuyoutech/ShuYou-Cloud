package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "充值套餐显示类")
public class PayRechargePackageVo extends BaseVo {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "套餐金额")
    private Long packagePrice;

    @Schema(description = "赠送算力")
    private Long packageCredit;

}
