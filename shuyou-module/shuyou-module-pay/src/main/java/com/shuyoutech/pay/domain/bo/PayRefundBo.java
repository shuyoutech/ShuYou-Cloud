package com.shuyoutech.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-01 19:45
 **/
@Data
@Schema(description = "申请退款请求类")
public class PayRefundBo implements Serializable {

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "退款金额，分单位")
    private Long amount;

    @Schema(description = "退款原因")
    private String reason;

    @Schema(description = "创建者ID")
    private String createUserId;

}
