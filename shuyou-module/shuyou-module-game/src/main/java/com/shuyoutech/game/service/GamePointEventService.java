package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GamePointEventBo;
import com.shuyoutech.game.domain.entity.GamePointEventEntity;
import com.shuyoutech.game.domain.vo.GamePointEventVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:39:57
 **/
public interface GamePointEventService extends SuperService<GamePointEventEntity, GamePointEventVo> {

    Query buildQuery(GamePointEventBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GamePointEventVo> page(PageQuery<GamePointEventBo> pageQuery);

    GamePointEventVo detail(String id);

    String saveGamePointEvent(GamePointEventBo bo);

    boolean updateGamePointEvent(GamePointEventBo bo);

    boolean deleteGamePointEvent(List<String> ids);

}
