package com.shuyoutech.crawler.service;

import com.shuyoutech.game.domain.entity.GameGoodsEntity;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 15:49
 **/
public interface WebmagicService {

    List<GameGoodsEntity> crawlerRecord(String crawlerId, String redisKey, String ip);

}
