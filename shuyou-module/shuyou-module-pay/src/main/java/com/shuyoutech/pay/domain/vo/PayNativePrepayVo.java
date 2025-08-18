package com.shuyoutech.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-01 19:45
 **/
@Data
@Schema(description = "微信PC端网页浏览器-Native下单类")
public class PayNativePrepayVo implements Serializable {

    @Schema(description = "订单ID")
    private String outTradeNo;

    @Schema(description = "支付的二维码链接地址")
    private String codeUrl;

}
