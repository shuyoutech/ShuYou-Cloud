package com.shuyoutech.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-01 19:45
 **/
@Data
@Schema(description = "查询单笔退款类")
public class QueryRefundBo implements Serializable {

    @NotBlank(message = "支付渠道不能为空")
    @Schema(description = "支付渠道")
    private String channelCode;

    @NotBlank(message = "商户退款单号不能为空")
    @Schema(description = "商户退款单号")
    private String outRefundNo;

}
