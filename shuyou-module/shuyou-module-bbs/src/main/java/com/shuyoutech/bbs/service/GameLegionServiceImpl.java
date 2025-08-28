package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameLegionBo;
import com.shuyoutech.bbs.domain.entity.GameLegionEntity;
import com.shuyoutech.bbs.domain.vo.GameLegionVo;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:02:06
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameLegionServiceImpl extends SuperServiceImpl<GameLegionEntity, GameLegionVo> implements GameLegionService {

    @Override
    public List<GameLegionVo> convertTo(List<GameLegionEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameLegionVo convertTo(GameLegionEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameLegionBo bo) {
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
        GameLegionEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameLegionVo> page(PageQuery<GameLegionBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameLegionVo detail(String id) {
        GameLegionEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameLegion(GameLegionBo bo) {
        GameLegionEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameLegion(GameLegionBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameLegion(List<String> ids) {
        return this.deleteByIds(ids);
    }

}