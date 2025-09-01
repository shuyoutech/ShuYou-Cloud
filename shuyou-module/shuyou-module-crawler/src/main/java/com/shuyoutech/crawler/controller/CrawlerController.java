package com.shuyoutech.crawler.controller;

import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.NumberUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.crawler.domain.bo.CrawlerGameGoodsBo;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.service.WebmagicService;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author YangChao
 * @date 2025-07-07 14:06
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("crawler")
@Tag(name = "CrawlerController", description = "爬虫API控制器")
public class CrawlerController {

    @PostMapping("gameGoods")
    @Operation(description = "爬虫游戏商品列表")
    public R<List<GameGoodsEntity>> gameGoods(@Validated @RequestBody CrawlerGameGoodsBo bo, HttpServletRequest request) {
        List<CompletableFuture<?>> futureList = CollectionUtils.newArrayList();
        String redisKey = IdUtil.getSnowflakeNextIdStr();
        String ip = JakartaServletUtil.getClientIP(request);

        Query query = new Query();
        query.addCriteria(Criteria.where("gameId").is(bo.getGameId()));
        query.addCriteria(Criteria.where("goodsType").is("YXB"));
        if (StringUtils.isNotBlank(bo.getGameArea())) {
            query.addCriteria(Criteria.where("gameArea").is(bo.getGameArea()));
        }
        if (StringUtils.isNotBlank(bo.getGameServer())) {
            query.addCriteria(Criteria.where("gameServer").is(bo.getGameServer()));
        }
        if (StringUtils.isNotBlank(bo.getGameRace())) {
            query.addCriteria(Criteria.where("gameRace").is(bo.getGameRace()));
        }
        List<CrawlerGameEntity> crawlerGameList = MongoUtils.selectList(query, CrawlerGameEntity.class);
        if (CollectionUtils.isEmpty(crawlerGameList)) {
            return R.success();
        }
        List<GameGoodsEntity> list = CollectionUtils.newArrayList();
        ExecutorService executor = GlobalThreadPool.getExecutor();
        CompletableFuture<List<GameGoodsEntity>> completableFuture;
        for (CrawlerGameEntity crawlerGame : crawlerGameList) {
            completableFuture = CompletableFuture.supplyAsync(() -> webmagicService.crawlerRecord(crawlerGame.getId(), redisKey, ip), executor).whenComplete((result, throwable) -> {
                // 任务完成时执行。用list存放任务的返回值
                if (CollectionUtils.isNotEmpty(result)) {
                    list.addAll(result);
                }
                // 触发异常
                if (throwable != null) {
                    log.error("webmagic ==================== throwable:{}", throwable.getMessage());
                }
            });
            futureList.add(completableFuture);
        }
        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("webmagic ==================== exception:{}", e.getMessage());
        }
        CollectionUtils.sort(list, (o1, o2) -> NumberUtils.compare(o2.getUnitPrice(), o1.getUnitPrice()));
        return R.success(list);
    }

    private final WebmagicService webmagicService;

}
