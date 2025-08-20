package com.shuyoutech.pay.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "支付渠道显示类")
public class PayChannelVo extends BaseVo {

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道配置")
    private String channelConfig;

}
