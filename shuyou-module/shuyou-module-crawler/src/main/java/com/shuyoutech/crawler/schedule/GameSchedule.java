package com.shuyoutech.crawler.schedule;

import com.shuyoutech.crawler.service.ScheduleExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        scheduleExecutorService.webmagicGameGmm();
    }

    @Operation(description = "每隔30s抓取373平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame373() {
        scheduleExecutorService.webmagicGame373();
    }

    @Operation(description = "每隔30s抓取5173平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame5173() {
        scheduleExecutorService.webmagicGame5173();
    }

    @Operation(description = "每隔30s抓取898平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame898() {
        scheduleExecutorService.webmagicGame898();
    }

    @Operation(description = "每隔30s抓取7881平台实时游戏币列表")
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void webmagicGame7881() {
        scheduleExecutorService.webmagicGame7881();
    }

    private final ScheduleExecutorService scheduleExecutorService;

}
