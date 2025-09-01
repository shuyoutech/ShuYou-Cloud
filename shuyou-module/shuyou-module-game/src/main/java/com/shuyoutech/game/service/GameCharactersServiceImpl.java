package com.shuyoutech.game.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.game.domain.bo.GameCharactersBo;
import com.shuyoutech.game.domain.entity.GameCharactersEntity;
import com.shuyoutech.game.domain.vo.GameCharactersVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:13:36
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameCharactersServiceImpl extends SuperServiceImpl<GameCharactersEntity, GameCharactersVo> implements GameCharactersService {

    @Override
    public List<GameCharactersVo> convertTo(List<GameCharactersEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameCharactersVo convertTo(GameCharactersEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameCharactersBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getGuildId())) {
            query.addCriteria(Criteria.where("guildId").is(bo.getGuildId()));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameCharactersEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameCharactersVo> page(PageQuery<GameCharactersBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameCharactersVo detail(String id) {
        GameCharactersEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameCharacters(GameCharactersBo bo) {
        GameCharactersEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameCharacters(GameCharactersBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameCharacters(List<String> ids) {
        return this.deleteByIds(ids);
    }

}