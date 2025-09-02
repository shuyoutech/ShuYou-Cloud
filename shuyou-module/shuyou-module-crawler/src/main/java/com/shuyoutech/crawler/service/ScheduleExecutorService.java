package com.shuyoutech.crawler.service;

import com.shuyoutech.api.enums.GamePlatformEnum;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.domain.entity.CrawlerRecordEntity;
import com.shuyoutech.crawler.processor.GameGoodsYxbPageListProcessor;
import com.shuyoutech.crawler.processor.GameGoodsYxbSelenium;
import com.shuyoutech.crawler.util.WebmagicUtils;
import com.shuyoutech.game.domain.entity.GameEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-07 13:22
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleExecutorService {

    public static final String REDIS_KEY_WEBMAGIC_GAME_GMM = "WEBMAGIC_GAME_GMM_KEY";
    public static final String REDIS_KEY_WEBMAGIC_GAME_373 = "WEBMAGIC_GAME_373_KEY";
    public static final String REDIS_KEY_WEBMAGIC_GAME_5173 = "WEBMAGIC_GAME_5173_KEY";
    public static final String REDIS_KEY_WEBMAGIC_GAME_898 = "WEBMAGIC_GAME_898_KEY";
    public static final String REDIS_KEY_WEBMAGIC_GAME_7881 = "WEBMAGIC_GAME_7881_KEY";
    public static Map<String, GameEntity> GAME_MAP = MapUtils.newHashMap();
    public static Map<String, CrawlerGameEntity> GAME_CRAWLER_MAP = MapUtils.newHashMap();
    public static List<CrawlerGameEntity> GAME_CRAWLER_GMM_LIST = CollectionUtils.newArrayList();
    public static List<CrawlerGameEntity> GAME_CRAWLER_DD373_LIST = CollectionUtils.newArrayList();
    public static List<CrawlerGameEntity> GAME_CRAWLER_UU898_LIST = CollectionUtils.newArrayList();
    public static List<CrawlerGameEntity> GAME_CRAWLER_5173_LIST = CollectionUtils.newArrayList();
    public static List<CrawlerGameEntity> GAME_CRAWLER_7881_LIST = CollectionUtils.newArrayList();

    @Operation(description = "抓取实时游戏币列表")
    public void webmagicGame(List<CrawlerGameEntity> crawlerGames, String redisKey) {
        if (CollectionUtils.isEmpty(crawlerGames)) {
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(redisKey, CrawlerGameEntity.class);
        if (crawlerGame == null) {
            RedisUtils.listRightPushAll(redisKey, crawlerGames);
            return;
        }
        webmagicCrawlerRecord(crawlerGame, null, null);
    }

    @Operation(description = "抓取规则入库且抓取")
    public void webmagicCrawlerRecord(CrawlerGameEntity crawlerGame, String id, HttpClientDownloader downloader) {
        GameEntity game = GAME_MAP.get(crawlerGame.getGameId());
        if (null == game) {
            log.error("webmagicCrawlerRecord ============= GAME_MAP is not contain gameId={}", crawlerGame.getGameId());
            return;
        }
        String url = crawlerGame.getCrawlUrl();
        if (StringUtils.isBlank(id)) {
            CrawlerRecordEntity crawlerRecord = MongoUtils.getById(crawlerGame.getId(), CrawlerRecordEntity.class);
            if (null == crawlerRecord) {
                CrawlerRecordEntity record = new CrawlerRecordEntity();
                record.setId(crawlerGame.getId());
                record.setStatus("0");
                record.setCreateTime(new Date());
                record.setPlatformId(crawlerGame.getPlatformId());
                record.setPlatformName(crawlerGame.getPlatformName());
                record.setGameId(crawlerGame.getGameId());
                record.setGameName(crawlerGame.getGameName());
                record.setGoodsType(crawlerGame.getGoodsType());
                record.setUrl(url);
                record.setCrawlerCount(0);
                record.setExecuteCount(0);
                MongoUtils.save(record);
                startCrawler(record);
            } else {
                CrawlerRecordEntity record = new CrawlerRecordEntity();
                record.setId(crawlerGame.getId());
                if ("1".equals(crawlerRecord.getStatus())) {
                    record.setStatus("0");
                    record.setCreateTime(new Date());
                    record.setPlatformId(crawlerGame.getPlatformId());
                    record.setPlatformName(crawlerGame.getPlatformName());
                    record.setGameId(crawlerGame.getGameId());
                    record.setGameName(crawlerGame.getGameName());
                    record.setGoodsType(crawlerGame.getGoodsType());
                    record.setUrl(url);
                    record.setCrawlerCount(0);
                    record.setExecuteCount(0);
                    MongoUtils.save(record);
                } else {
                    record.setExecuteCount(crawlerRecord.getExecuteCount() + 1);
                    MongoUtils.patch(record);
                }
                startCrawler(crawlerRecord);
            }
        } else {
            Request request = new Request(url);
            Map<String, Object> extraMap = MapUtils.newHashMap();
            extraMap.put("crawler", crawlerGame);
            extraMap.put("game", game);
            extraMap.put("platformId", crawlerGame.getPlatformId());
            extraMap.put("id", id);
            request.setExtras(extraMap);
            if (GamePlatformEnum.PLATFORM_GMM.getValue().equals(crawlerGame.getPlatformId())) {
                gameGoodsYxbSelenium.dynamicParseGmmNewYxb(request);
            } else {
                Spider.create(new GameGoodsYxbPageListProcessor()).setDownloader(downloader).addRequest(request).thread(1).run();
            }
        }
    }

    @Operation(description = "开始抓取")
    public void startCrawler(CrawlerRecordEntity record) {
        Request request = new Request(record.getUrl());
        Map<String, Object> extraMap = MapUtils.newHashMap();
        extraMap.put("crawler", GAME_CRAWLER_MAP.get(record.getId()));
        extraMap.put("game", GAME_MAP.get(record.getGameId()));
        extraMap.put("platformId", record.getPlatformId());
        extraMap.put("recordId", record.getId());
        request.setExtras(extraMap);
        if (GamePlatformEnum.PLATFORM_GMM.getValue().equals(record.getPlatformId())) {
            gameGoodsYxbSelenium.dynamicParseGmmNewYxb(request);
        } else {
            Spider.create(new GameGoodsYxbPageListProcessor()).setDownloader(WebmagicUtils.getQingGuoTimeProxy()).addRequest(request).thread(1).run();
        }
    }

    private final GameGoodsYxbSelenium gameGoodsYxbSelenium;
}
