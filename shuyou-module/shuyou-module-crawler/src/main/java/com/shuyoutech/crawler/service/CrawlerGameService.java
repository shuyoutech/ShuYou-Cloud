package com.shuyoutech.crawler.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.crawler.domain.bo.CrawlerGameBo;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.domain.vo.CrawlerGameVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
public interface CrawlerGameService extends SuperService<CrawlerGameEntity, CrawlerGameVo> {

    Query buildQuery(CrawlerGameBo bo);

    PageResult<CrawlerGameVo> page(PageQuery<CrawlerGameBo> pageQuery);

    CrawlerGameVo detail(String id);

    String saveCrawlerGame(CrawlerGameBo bo);

    boolean updateCrawlerGame(CrawlerGameBo bo);

    boolean deleteCrawlerGame(List<String> ids);

}
