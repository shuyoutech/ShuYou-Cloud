package com.shuyoutech.crawler.schedule;

import com.shuyoutech.crawler.service.ScheduleExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.shuyoutech.crawler.service.ScheduleExecutorService.*;

/**
 * @author YangChao
 * @date 2025-07-07 13:33
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class GameSchedule {

    @Operation(description = "每隔30s抓取GMM平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGameGmm() {
        scheduleExecutorService.webmagicGame(GAME_CRAWLER_GMM_LIST, REDIS_KEY_WEBMAGIC_GAME_GMM);
    }

    @Operation(description = "每隔30s抓取373平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame373() {
        scheduleExecutorService.webmagicGame(GAME_CRAWLER_DD373_LIST, REDIS_KEY_WEBMAGIC_GAME_373);
    }

    @Operation(description = "每隔30s抓取5173平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame5173() {
        scheduleExecutorService.webmagicGame(GAME_CRAWLER_5173_LIST, REDIS_KEY_WEBMAGIC_GAME_5173);
    }

    @Operation(description = "每隔30s抓取898平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame898() {
        scheduleExecutorService.webmagicGame(GAME_CRAWLER_UU898_LIST, REDIS_KEY_WEBMAGIC_GAME_898);
    }

    @Operation(description = "每隔30s抓取7881平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame7881() {
        scheduleExecutorService.webmagicGame(GAME_CRAWLER_7881_LIST, REDIS_KEY_WEBMAGIC_GAME_7881);
    }

    private final ScheduleExecutorService scheduleExecutorService;

}
