package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GameGoodsBo;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import com.shuyoutech.game.domain.vo.GameGoodsVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 11:43:29
 **/
public interface GameGoodsService extends SuperService<GameGoodsEntity, GameGoodsVo> {

    Query buildQuery(GameGoodsBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameGoodsVo> page(PageQuery<GameGoodsBo> pageQuery);

    GameGoodsVo detail(String id);

    String saveGameGoods(GameGoodsBo bo);

    boolean updateGameGoods(GameGoodsBo bo);

    boolean deleteGameGoods(List<String> ids);

}
