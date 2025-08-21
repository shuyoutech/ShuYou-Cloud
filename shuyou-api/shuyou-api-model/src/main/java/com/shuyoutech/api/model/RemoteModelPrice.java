package com.shuyoutech.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author YangChao
 * @date 2025-07-14 21:54
 **/
@Data
public class RemoteModelPrice implements Serializable {

    @Schema(description = "收费规则")
    private String feeRule;

    @Schema(description = "提示输入token价格")
    private BigDecimal tokenPrice;

    @Schema(description = "token价格单位")
    private String tokenPriceUnit;

    @Schema(description = "token货币单位")
    private String currencyUnit;

}
