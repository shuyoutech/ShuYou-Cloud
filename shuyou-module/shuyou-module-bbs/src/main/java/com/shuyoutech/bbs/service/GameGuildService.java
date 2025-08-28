package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameGuildBo;
import com.shuyoutech.bbs.domain.entity.GameGuildEntity;
import com.shuyoutech.bbs.domain.vo.GameGuildVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 11:52:43
 **/
public interface GameGuildService extends SuperService<GameGuildEntity, GameGuildVo> {

    Query buildQuery(GameGuildBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameGuildVo> page(PageQuery<GameGuildBo> pageQuery);

    GameGuildVo detail(String id);

    String saveGameGuild(GameGuildBo bo);

    boolean updateGameGuild(GameGuildBo bo);

    boolean deleteGameGuild(List<String> ids);

    boolean statusGameGuild(String id, String status);

}
