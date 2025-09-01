package com.shuyoutech.crawler.service;

import com.shuyoutech.common.core.constant.StringConstants;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.util.WebmagicUtils;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.List;

import static com.shuyoutech.crawler.service.ScheduleExecutorService.GAME_CRAWLER_MAP;

/**
 * @author YangChao
 * @date 2025-07-07 15:54
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class WebmagicServiceImpl implements WebmagicService {

    @Override
    public List<GameGoodsEntity> crawlerRecord(String crawlerId, String redisKey, String ip) {
        log.info("crawlerRecord ============== crawlerId:{}, redisKey:{}, ip:{}", crawlerId, redisKey, ip);
        CrawlerGameEntity crawlerGame = GAME_CRAWLER_MAP.get(crawlerId);
        if (null == crawlerGame) {
            return null;
        }
        HttpClientDownloader downloader = WebmagicUtils.getQingGuoIpProxy();
        scheduleExecutorService.webmagicCrawlerRecord(crawlerGame, redisKey, downloader);
        String key = crawlerGame.getPlatformId() + StringConstants.HYPHEN + redisKey;
        List<GameGoodsEntity> list = RedisUtils.listRange(key, GameGoodsEntity.class);
        if (CollectionUtils.isEmpty(list)) {
            log.info("crawlerRecord ============== crawlerId:{}, platformGame:{}, count:{}", crawlerId, crawlerGame.getPlatformName(), 0);
            return null;
        }
        log.info("crawlerRecord ============== crawlerId:{}, platformGame:{}, count:{}", crawlerId, crawlerGame.getPlatformName(), list.size());
        return list;
    }

    private final ScheduleExecutorService scheduleExecutorService;

}
