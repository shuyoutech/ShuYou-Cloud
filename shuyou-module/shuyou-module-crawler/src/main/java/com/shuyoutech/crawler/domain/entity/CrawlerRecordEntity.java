package com.shuyoutech.crawler.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "crawler_record")
@Schema(description = "爬虫记录表类")
public class CrawlerRecordEntity extends BaseEntity<CrawlerRecordEntity> {

    @Schema(description = "状态 0-待爬取,1-爬取成功")
    private String status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "平台id")
    private String platformId;

    @Schema(description = "平台名称")
    private String platformName;

    @Schema(description = "游戏id")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "商品类型")
    private String goodsType;

    @Schema(description = "请求地址")
    private String url;

    @Schema(description = "抓取数量")
    private Integer crawlerCount;

    @Schema(description = "执行次数")
    private Integer executeCount;

}
