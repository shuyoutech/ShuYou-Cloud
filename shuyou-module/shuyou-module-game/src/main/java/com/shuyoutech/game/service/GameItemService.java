package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GameItemBo;
import com.shuyoutech.game.domain.entity.GameItemEntity;
import com.shuyoutech.game.domain.vo.GameItemVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:34:21
 **/
public interface GameItemService extends SuperService<GameItemEntity, GameItemVo> {

    Query buildQuery(GameItemBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameItemVo> page(PageQuery<GameItemBo> pageQuery);

    GameItemVo detail(String id);

    String saveGameItem(GameItemBo bo);

    boolean updateGameItem(GameItemBo bo);

    boolean deleteGameItem(List<String> ids);

}
