package com.shuyoutech.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-01 19:45
 **/
@Data
@Schema(description = "支付下单请求类")
public class PayPrepayBo implements Serializable {

    @NotBlank(message = "支付渠道不能为空")
    @Schema(description = "支付渠道")
    private String channelCode;

    @NotNull(message = "支付金额不能为空")
    @Schema(description = "支付金额,单位分")
    private Integer amount;

}
