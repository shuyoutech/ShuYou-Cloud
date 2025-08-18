package com.shuyoutech.pay.domain.bo;

import com.shuyoutech.api.enums.PayNotifyStatusEnum;
import com.shuyoutech.api.enums.PayTradeTypeEnum;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.pay.domain.entity.PayNotifyRecordEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-23 16:26:15
 **/
@Data
@AutoMapper(target = PayNotifyRecordEntity.class)
@Schema(description = "支付通知记录类")
public class PayNotifyRecordBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    /**
     * 枚举 {@link PayNotifyStatusEnum}
     */
    @Schema(description = "通知状态")
    private String status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "订单ID")
    private String orderId;

    /**
     * 枚举 {@link PayTradeTypeEnum}
     * JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
     */
    @Schema(description = "交易类型:JSAPI：公众号支付,NATIVE：扫码支付,APP：APP支付,MICROPAY：付款码支付,MWEB：H5支付,FACEPAY：刷脸支付")
    private String tradeType;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "商户订单号")
    private String mchOrderNo;

    @Schema(description = "支付金额,单位分")
    private Integer amount;

    @Schema(description = "通知响应结果")
    private String notifyResult;

}
