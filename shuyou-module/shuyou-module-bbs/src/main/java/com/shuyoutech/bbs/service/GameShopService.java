package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameShopBo;
import com.shuyoutech.bbs.domain.entity.GameShopEntity;
import com.shuyoutech.bbs.domain.vo.GameShopVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
public interface GameShopService extends SuperService<GameShopEntity, GameShopVo> {

    Query buildQuery(GameShopBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameShopVo> page(PageQuery<GameShopBo> pageQuery);

    GameShopVo detail(String id);

    String saveGameShop(GameShopBo bo);

    boolean updateGameShop(GameShopBo bo);

    boolean deleteGameShop(List<String> ids);

}
