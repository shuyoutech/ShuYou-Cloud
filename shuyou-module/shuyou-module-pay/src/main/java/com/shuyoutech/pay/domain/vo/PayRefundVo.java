package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-20 19:57:14
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "支付退款显示类")
public class PayRefundVo extends BaseVo {

    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建者ID")
    private String createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "支付金额，分单位")
    private Long payPrice;

    @Schema(description = "退款金额，分单位")
    private Long refundPrice;

    @Schema(description = "退款原因")
    private String reason;

    @Schema(description = "退款成功时间")
    private Date successTime;

}
