package com.shuyoutech.crawler.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.crawler.domain.bo.CrawlerGameBo;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.domain.vo.CrawlerGameVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerGameServiceImpl extends SuperServiceImpl<CrawlerGameEntity, CrawlerGameVo> implements CrawlerGameService {

    @Override
    public List<CrawlerGameVo> convertTo(List<CrawlerGameEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public CrawlerGameVo convertTo(CrawlerGameEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(CrawlerGameBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<CrawlerGameVo> page(PageQuery<CrawlerGameBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public CrawlerGameVo detail(String id) {
        CrawlerGameEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveCrawlerGame(CrawlerGameBo bo) {
        CrawlerGameEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateCrawlerGame(CrawlerGameBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteCrawlerGame(List<String> ids) {
        return this.deleteByIds(ids);
    }

}