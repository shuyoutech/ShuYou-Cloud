package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GameTradingPlatformBo;
import com.shuyoutech.game.domain.entity.GameTradingPlatformEntity;
import com.shuyoutech.game.domain.vo.GameTradingPlatformVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
public interface GameTradingPlatformService extends SuperService<GameTradingPlatformEntity, GameTradingPlatformVo> {

    Query buildQuery(GameTradingPlatformBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameTradingPlatformVo> page(PageQuery<GameTradingPlatformBo> pageQuery);

    GameTradingPlatformVo detail(String id);

    String saveGameShop(GameTradingPlatformBo bo);

    boolean updateGameShop(GameTradingPlatformBo bo);

    boolean deleteGameShop(List<String> ids);

}
