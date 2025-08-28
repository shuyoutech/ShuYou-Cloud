package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GamePointEventBo;
import com.shuyoutech.bbs.domain.entity.GamePointEventEntity;
import com.shuyoutech.bbs.domain.vo.GamePointEventVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
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
