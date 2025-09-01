package com.shuyoutech.crawler.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.crawler.domain.vo.CrawlerGameVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CrawlerGameVo.class)
@Document(collection = "crawler_game")
@Schema(description = "爬虫游戏表类")
public class CrawlerGameEntity extends BaseEntity<CrawlerGameEntity> {

    @Schema(description = "平台id")
    private String platformId;

    @Schema(description = "平台名称")
    private String platformName;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "相应平台里游戏ID")
    private String platformGameId;

    @Schema(description = "商品类型 账号、游戏币、装备、点券")
    private String goodsType;

    @Schema(description = "游戏区")
    private String gameArea;

    @Schema(description = "游戏服")
    private String gameServer;

    @Schema(description = "游戏种族")
    private String gameRace;

    @Schema(description = "爬取URL地址")
    private String crawlUrl;

    @Schema(description = "备用字段1")
    private String field1;

    @Schema(description = "备用字段2")
    private String field2;

    @Schema(description = "备用字段3")
    private String field3;

}
