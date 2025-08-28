package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameBo;
import com.shuyoutech.bbs.domain.entity.GameEntity;
import com.shuyoutech.bbs.domain.vo.GameGoodsUnitPriceVo;
import com.shuyoutech.bbs.domain.vo.GameVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-10 09:46:51
 **/
public interface GameService extends SuperService<GameEntity, GameVo> {

    Query buildQuery(GameBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameVo> page(PageQuery<GameBo> pageQuery);

    GameVo detail(String id);

    String saveGame(GameBo bo);

    boolean updateGame(GameBo bo);

    boolean deleteGame(List<String> ids);

    Map<String, Object> querySearch(GameBo bo);

    List<GameGoodsUnitPriceVo> latestUnitPrice(String gameId);

}
