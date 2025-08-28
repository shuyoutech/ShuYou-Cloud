package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameGoodsBo;
import com.shuyoutech.bbs.domain.entity.GameGoodsEntity;
import com.shuyoutech.bbs.domain.vo.GameGoodsVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
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
