package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameLegionBo;
import com.shuyoutech.bbs.domain.entity.GameLegionEntity;
import com.shuyoutech.bbs.domain.vo.GameLegionVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
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
