package com.shuyoutech.pay.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.pay.domain.entity.PayChannelEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-20 13:04:31
 **/
@Data
@AutoMapper(target = PayChannelEntity.class)
@Schema(description = "支付渠道类")
public class PayChannelBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道配置")
    private String channelConfig;
}
