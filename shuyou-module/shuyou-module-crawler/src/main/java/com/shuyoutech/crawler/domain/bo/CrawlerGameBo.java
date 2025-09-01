package com.shuyoutech.crawler.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Data
@Schema(description = "爬虫游戏BO")
public class CrawlerGameBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "平台id")
    private String platformId;

    @Schema(description = "游戏id")
    private String gameId;

}
