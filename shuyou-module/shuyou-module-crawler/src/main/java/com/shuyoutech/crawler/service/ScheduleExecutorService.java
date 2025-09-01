package com.shuyoutech.crawler.service;

import com.shuyoutech.api.enums.GameGoodsTypeEnum;
import com.shuyoutech.api.enums.GamePlatformEnum;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.HttpClientUtils;
import com.shuyoutech.common.core.util.MapUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.domain.entity.CrawlerRecordEntity;
import com.shuyoutech.crawler.processor.GameGoodsYxbPageListProcessor;
import com.shuyoutech.crawler.util.WebmagicUtils;
import com.shuyoutech.game.domain.entity.GameEntity;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
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
    public List<CrawlerGameEntity> GAME_CRAWLER_GMM_LIST = CollectionUtils.newArrayList();
    public List<CrawlerGameEntity> GAME_CRAWLER_DD373_LIST = CollectionUtils.newArrayList();
    public List<CrawlerGameEntity> GAME_CRAWLER_UU898_LIST = CollectionUtils.newArrayList();
    public List<CrawlerGameEntity> GAME_CRAWLER_5173_LIST = CollectionUtils.newArrayList();
    public List<CrawlerGameEntity> GAME_CRAWLER_7881_LIST = CollectionUtils.newArrayList();

    @PostConstruct
    public void init() {
        List<CrawlerGameEntity> crawlers = MongoUtils.selectList(CrawlerGameEntity.class);
        for (CrawlerGameEntity crawler : crawlers) {
            if (!GameGoodsTypeEnum.GOODS_TYPE_YXB.getValue().equalsIgnoreCase(crawler.getGoodsType())) {
                continue;
            }
            if (GamePlatformEnum.PLATFORM_GMM.getValue().equals(crawler.getPlatformId())) {
                GAME_CRAWLER_GMM_LIST.add(crawler);
            } else if (GamePlatformEnum.PLATFORM_DD373.getValue().equals(crawler.getPlatformId())) {
                GAME_CRAWLER_DD373_LIST.add(crawler);
            } else if (GamePlatformEnum.PLATFORM_UU898.getValue().equals(crawler.getPlatformId())) {
                GAME_CRAWLER_UU898_LIST.add(crawler);
            } else if (GamePlatformEnum.PLATFORM_5173.getValue().equals(crawler.getPlatformId())) {
                GAME_CRAWLER_5173_LIST.add(crawler);
            } else if (GamePlatformEnum.PLATFORM_7881.getValue().equals(crawler.getPlatformId())) {
                GAME_CRAWLER_7881_LIST.add(crawler);
            }
            GAME_CRAWLER_MAP.put(crawler.getId(), crawler);
        }
    }

    @Operation(description = "抓取GMM平台实时游戏币列表")
    public void webmagicGameGmm() {
        if (CollectionUtils.isEmpty(GAME_CRAWLER_GMM_LIST)) {
            log.error("webmagicGameGmm ============= GAME_CRAWLER_GMM_LIST is null");
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(REDIS_KEY_WEBMAGIC_GAME_GMM, CrawlerGameEntity.class);
        if (crawlerGame == null) {
            RedisUtils.listRightPushAll(REDIS_KEY_WEBMAGIC_GAME_GMM, GAME_CRAWLER_GMM_LIST);
            return;
        }
        webmagicCrawlerRecord(crawlerGame, null, null);
    }

    @Operation(description = "抓取373平台实时游戏币列表")
    public void webmagicGame373() {
        if (CollectionUtils.isEmpty(GAME_CRAWLER_DD373_LIST)) {
            log.error("webmagicGame373 ============= GAME_CRAWLER_DD373_LIST is null");
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(REDIS_KEY_WEBMAGIC_GAME_373, CrawlerGameEntity.class);
        if (null == crawlerGame) {
            RedisUtils.listRightPushAll(REDIS_KEY_WEBMAGIC_GAME_373, GAME_CRAWLER_DD373_LIST);
            return;
        }
        webmagicCrawlerRecord(crawlerGame, null, null);
    }

    @Operation(description = "抓取5173平台实时游戏币列表")
    public void webmagicGame5173() {
        if (CollectionUtils.isEmpty(GAME_CRAWLER_5173_LIST)) {
            log.error("webmagicGame5173 ============= GAME_CRAWLER_5173_LIST is null");
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(REDIS_KEY_WEBMAGIC_GAME_5173, CrawlerGameEntity.class);
        if (null == crawlerGame) {
            RedisUtils.listRightPushAll(REDIS_KEY_WEBMAGIC_GAME_5173, GAME_CRAWLER_5173_LIST);
            return;
        }
        webmagicCrawlerRecord(crawlerGame, null, null);
    }

    @Operation(description = "抓取898平台实时游戏币列表")
    public void webmagicGame898() {
        if (CollectionUtils.isEmpty(GAME_CRAWLER_UU898_LIST)) {
            log.error("webmagicGame898 ============= GAME_CRAWLER_UU898_LIST is null");
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(REDIS_KEY_WEBMAGIC_GAME_898, CrawlerGameEntity.class);
        if (null == crawlerGame) {
            RedisUtils.listRightPushAll(REDIS_KEY_WEBMAGIC_GAME_898, GAME_CRAWLER_UU898_LIST);
            return;
        }
        webmagicCrawlerRecord(crawlerGame, null, null);
    }

    @Operation(description = "抓取7881平台实时游戏币列表")
    public void webmagicGame7881() {
        if (CollectionUtils.isEmpty(GAME_CRAWLER_7881_LIST)) {
            log.error("webmagicGame7881 ============= GAME_CRAWLER_7881_LIST is null");
            return;
        }
        CrawlerGameEntity crawlerGame = RedisUtils.listRightPop(REDIS_KEY_WEBMAGIC_GAME_7881, CrawlerGameEntity.class);
        if (null == crawlerGame) {
            RedisUtils.listRightPushAll(REDIS_KEY_WEBMAGIC_GAME_7881, GAME_CRAWLER_7881_LIST);
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
        String url;
        if (GamePlatformEnum.PLATFORM_GMM.getValue().equals(crawlerGame.getPlatformId())) {
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("src_code", "7");
            if (GameGoodsTypeEnum.GOODS_TYPE_ZH.getValue().equalsIgnoreCase(crawlerGame.getGoodsType())) {
                map.put("order_type", "modify_time");
                map.put("order_dir", "d");
                map.put("goods_types", "10");
            } else if (GameGoodsTypeEnum.GOODS_TYPE_YXB.getValue().equalsIgnoreCase(crawlerGame.getGoodsType())) {
                map.put("order_type", "unit_price");
                map.put("order_dir", "a");
                map.put("goods_types", "5,35");
            }
            map.put("searchProperties", StringUtils.format("{\"p_race\":\"[{}]\"}", crawlerGame.getField3()));
            map.put("area_id_groups", StringUtils.format("[{\"area_id\":{},\"groups\":[{}]}]", crawlerGame.getField1(), crawlerGame.getField2()));
            map.put("game_id", Long.valueOf(crawlerGame.getPlatformGameId()));
            map.put("page", 1);
            map.put("limit", 20);
            url = StringUtils.build("https://www.gmmsj.com/gatew/gmmGoodsGW/goodsListV2", "?", HttpClientUtils.encodeParameters(map));
        } else {
            url = crawlerGame.getCrawlUrl();
        }
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
            Spider.create(new GameGoodsYxbPageListProcessor()).setDownloader(downloader).addRequest(request).thread(1).run();
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
        Spider.create(new GameGoodsYxbPageListProcessor()).setDownloader(WebmagicUtils.getQingGuoTimeProxy()).addRequest(request).thread(1).run();
    }

}
