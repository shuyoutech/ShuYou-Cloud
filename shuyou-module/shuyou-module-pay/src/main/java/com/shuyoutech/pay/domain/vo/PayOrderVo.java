package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.api.enums.PayOrderStatusEnum;
import com.shuyoutech.api.enums.PayTradeTypeEnum;
import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-23 11:04:39
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "支付订单显示类")
public class PayOrderVo extends BaseVo {

    /**
     * 枚举 {@link PayOrderStatusEnum}
     * 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭
     */
    @Schema(description = "支付状态")
    private String status;

    @Schema(description = "支付状态")
    private String statusName;

    /**
     * 枚举 {@link PayTradeTypeEnum}
     * JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
     */
    @Schema(description = "交易类型")
    private String tradeType;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建者ID")
    private String createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "商户订单号")
    private String transactionId;

    @Schema(description = "支付金额,单位分")
    private Integer amount;

    @Schema(description = "支付金额,单位元")
    private String amountStr;

    @Schema(description = "订单失效时间")
    private Date expiredTime;

    @Schema(description = "订单支付成功时间")
    private Date successTime;

}
