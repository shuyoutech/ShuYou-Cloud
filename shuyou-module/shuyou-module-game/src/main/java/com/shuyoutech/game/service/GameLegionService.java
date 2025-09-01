package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GameLegionBo;
import com.shuyoutech.game.domain.entity.GameLegionEntity;
import com.shuyoutech.game.domain.vo.GameLegionVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:02:06
 **/
public interface GameLegionService extends SuperService<GameLegionEntity, GameLegionVo> {

    Query buildQuery(GameLegionBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameLegionVo> page(PageQuery<GameLegionBo> pageQuery);

    GameLegionVo detail(String id);

    String saveGameLegion(GameLegionBo bo);

    boolean updateGameLegion(GameLegionBo bo);

    boolean deleteGameLegion(List<String> ids);

}
