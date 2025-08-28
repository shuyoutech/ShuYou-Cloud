package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GamePlayerBo;
import com.shuyoutech.bbs.domain.bo.GamePlayerPointRegisterBo;
import com.shuyoutech.bbs.domain.bo.GamePlayerPointSettleBo;
import com.shuyoutech.bbs.domain.entity.GamePlayerEntity;
import com.shuyoutech.bbs.domain.vo.GamePlayerVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.model.TreeOption;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
public interface GamePlayerService extends SuperService<GamePlayerEntity, GamePlayerVo> {

    Query buildQuery(GamePlayerBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GamePlayerVo> page(PageQuery<GamePlayerBo> pageQuery);

    GamePlayerVo detail(String id);

    String saveGamePlayer(GamePlayerBo bo);

    boolean updateGamePlayer(GamePlayerBo bo);

    boolean deleteGamePlayer(List<String> ids);

    List<TreeOption> tree();

    void pointRegister(GamePlayerPointRegisterBo bo);

    void pointSettle(GamePlayerPointSettleBo bo);

    Map<String, BigDecimal> calPlayerPoint(Set<String> playerIds, long start, long end);

    Map<String, BigDecimal> calPlayerPointMonth(String guildId, String month);

    Set<String> matchPlayer(List<String> eventFileIds);

}
