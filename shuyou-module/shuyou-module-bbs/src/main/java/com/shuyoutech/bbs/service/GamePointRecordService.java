package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GamePointRecordBo;
import com.shuyoutech.bbs.domain.entity.GamePointRecordEntity;
import com.shuyoutech.bbs.domain.vo.GamePointRecordVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:49:10
 **/
public interface GamePointRecordService extends SuperService<GamePointRecordEntity, GamePointRecordVo> {

    Query buildQuery(GamePointRecordBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GamePointRecordVo> page(PageQuery<GamePointRecordBo> pageQuery);

    GamePointRecordVo detail(String id);

    String saveGamePointRecord(GamePointRecordBo bo);

    boolean updateGamePointRecord(GamePointRecordBo bo);

    boolean deleteGamePointRecord(List<String> ids);

}
