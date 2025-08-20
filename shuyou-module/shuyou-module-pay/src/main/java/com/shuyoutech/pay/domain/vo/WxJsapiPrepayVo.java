package com.shuyoutech.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-01 19:45
 **/
@Data
@Schema(description = "JSAPI/小程序下单类")
public class WxJsapiPrepayVo implements Serializable {

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "appID")
    private String appId;

    @Schema(description = "时间戳")
    private String timestamp;

    @Schema(description = "随机字符串")
    private String nonceStr;

    @Schema(description = "订单详情扩展")
    private String packageVal;

    @Schema(description = "签名方式")
    private String signType;

    @Schema(description = "签名")
    private String paySign;

}
