package com.shuyoutech.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-11 11:18:54
 **/
@Data
@Schema(description = "模型类")
public class RemoteModel implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "供应商")
    private String provider;

    @Schema(description = "模型类型")
    private String modelType;

    @Schema(description = "模型类型集合")
    private List<String> modelTypes;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "模型别名")
    private String modelAlias;

    @Schema(description = "输入支持")
    private List<String> inputs;

    @Schema(description = "输出支持")
    private List<String> outputs;

    @Schema(description = "模型描述")
    private String modelDesc;

    @Schema(description = "代理地址")
    private String baseUrl;

    @Schema(description = "API 密钥")
    private String apiKey;

    @Schema(description = "模型标签")
    private String modelTag;

    @Schema(description = "模型标签")
    private List<String> modelTags;

    @Schema(description = "是否热门模型")
    private Boolean beenHot;

    @Schema(description = "是否支持互联网搜索")
    private Boolean enableSearch;

    @Schema(description = "是否支持思考模式")
    private Boolean enableThinking;

    @Schema(title = "计费类型")
    private String chargeType;

    @Schema(title = "价格集合")
    private List<RemoteModelPrice> prices;

}
