package com.shuyoutech.aigc.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-20 21:19:49
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "对话消息显示类")
public class AigcChatMessageVo extends BaseVo {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "会话ID")
    private String conversationId;

    @Schema(description = "供应商")
    private String provider;

    @Schema(description = "模型")
    private String modelName;

    @Schema(description = "模型功能接口")
    private String modelFunction;

    @Schema(description = "是否开启多轮对话")
    private Boolean enableMemory;

    @Schema(description = "是否开启深度思考")
    private Boolean enableThinking;

    @Schema(description = "是否开启联网搜索")
    private Boolean enableSearch;

    @Schema(description = "用户发送消息")
    private String userMessage;

    @Schema(description = "AI返回消息")
    private String assistantMessage;

    @Schema(description = "请求时间")
    private Date requestTime;

    @Schema(description = "响应时间")
    private Date responseTime;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "请求地区")
    private String ipRegion;

    @Schema(description = "对话运行时间")
    private BigDecimal durationSeconds;

    @Schema(description = "输入Token数量")
    private Integer inputTokenCount;

    @Schema(description = "命中上下文缓存的 token 数")
    private Integer inputCacheHitTokenCount;

    @Schema(description = "未命中上下文缓存的 token 数")
    private Integer inputCacheMissTokenCount;

    @Schema(description = "输入累计价格")
    private BigDecimal inputPrice;

    @Schema(description = "输出Token数量")
    private Integer outputTokenCount;

    @Schema(description = "输出累计价格")
    private BigDecimal outputPrice;

    @Schema(description = "总共Token数量")
    private Integer totalTokenCount;

    @Schema(description = "总共价格")
    private BigDecimal totalPrice;

}
