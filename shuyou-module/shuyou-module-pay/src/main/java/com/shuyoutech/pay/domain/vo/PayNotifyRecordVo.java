package com.shuyoutech.pay.domain.vo;

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
 * @date 2025-07-23 16:26:15
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "支付通知记录显示类")
public class PayNotifyRecordVo extends BaseVo {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "通知请求headers")
    private String requestHeaders;

    @Schema(description = "通知请求body")
    private String requestBody;

    @Schema(description = "通知请求params")
    private String requestParams;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "商户订单号-商户下单时传入的商户系统内部订单号")
    private String outTradeNo;

    /**
     * 枚举 {@link PayTradeTypeEnum}
     * JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
     */
    @Schema(description = "交易类型:JSAPI：公众号支付,NATIVE：扫码支付,APP：APP支付,MICROPAY：付款码支付,MWEB：H5支付,FACEPAY：刷脸支付")
    private String tradeType;

    @Schema(description = "交易状态")
    private String tradeState;

    @Schema(description = "微信支付订单号-微信支付侧订单的唯一标识")
    private String transactionId;

    @Schema(description = "支付金额,单位分")
    private Integer amount;

}
