package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.model.TreeOption;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GamePlayerBo;
import com.shuyoutech.game.domain.bo.GamePlayerPointRegisterBo;
import com.shuyoutech.game.domain.bo.GamePlayerPointSettleBo;
import com.shuyoutech.game.domain.entity.GamePlayerEntity;
import com.shuyoutech.game.domain.vo.GamePlayerVo;
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
