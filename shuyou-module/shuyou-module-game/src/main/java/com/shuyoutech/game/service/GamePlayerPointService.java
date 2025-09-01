package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GamePlayerPointBo;
import com.shuyoutech.game.domain.entity.GamePlayerPointEntity;
import com.shuyoutech.game.domain.vo.GamePlayerPointVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
public interface GamePlayerPointService extends SuperService<GamePlayerPointEntity, GamePlayerPointVo> {

    Query buildQuery(GamePlayerPointBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GamePlayerPointVo> page(PageQuery<GamePlayerPointBo> pageQuery);

    GamePlayerPointVo detail(String id);

    String saveGamePlayerPoint(GamePlayerPointBo bo);

    boolean updateGamePlayerPoint(GamePlayerPointBo bo);

    boolean deleteGamePlayerPoint(List<String> ids);

}
