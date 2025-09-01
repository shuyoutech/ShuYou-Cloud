package com.shuyoutech.game.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.game.domain.bo.GameCharactersBo;
import com.shuyoutech.game.domain.entity.GameCharactersEntity;
import com.shuyoutech.game.domain.vo.GameCharactersVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:13:36
 **/
public interface GameCharactersService extends SuperService<GameCharactersEntity, GameCharactersVo> {

    Query buildQuery(GameCharactersBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<GameCharactersVo> page(PageQuery<GameCharactersBo> pageQuery);

    GameCharactersVo detail(String id);

    String saveGameCharacters(GameCharactersBo bo);

    boolean updateGameCharacters(GameCharactersBo bo);

    boolean deleteGameCharacters(List<String> ids);

}
