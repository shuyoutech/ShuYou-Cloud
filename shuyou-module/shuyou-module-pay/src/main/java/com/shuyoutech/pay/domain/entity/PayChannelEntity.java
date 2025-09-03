package com.shuyoutech.pay.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.pay.domain.vo.PayChannelVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PayChannelVo.class)
@Document(collection = "pay_channel")
@Schema(description = "支付渠道表类")
public class PayChannelEntity extends BaseEntity<PayChannelEntity> {

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道配置")
    private String channelConfig;

}
