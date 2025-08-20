package com.shuyoutech.pay.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author YangChao
 * @date 2025-07-23 10:13
 **/
@Data
public class WxPayConfig {

    @Schema(description = "APPID")
    private String appid;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "商户API证书序列号")
    private String merchantSerialNumber;

    @Schema(description = "商户私钥文件")
    private String privateKeyFromPath;

    @Schema(description = "微信支付公钥的存放路径")
    private String publicKeyFromPath;

    @Schema(description = "微信支付公钥ID")
    private String publicKeyId;

    @Schema(description = "APIv3密钥")
    private String apiV3Key;

    @Schema(description = "微信服务器地址")
    private String domain;

    @Schema(description = "接收结果通知地址")
    private String notifyUrl;

}
