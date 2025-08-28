package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GamePlayerPointBo;
import com.shuyoutech.bbs.domain.entity.GamePlayerPointEntity;
import com.shuyoutech.bbs.domain.vo.GamePlayerPointVo;
import com.shuyoutech.common.core.util.CollectionUtils;
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
 * @date 2025-07-10 12:43:36
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GamePlayerPointServiceImpl extends SuperServiceImpl<GamePlayerPointEntity, GamePlayerPointVo> implements GamePlayerPointService {

    @Override
    public List<GamePlayerPointVo> convertTo(List<GamePlayerPointEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GamePlayerPointVo convertTo(GamePlayerPointEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GamePlayerPointBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getPlayerId())) {
            query.addCriteria(Criteria.where("playerId").is(bo.getPlayerId()));
        }
        if (CollectionUtils.isNotEmpty(bo.getPlayerIds())) {
            query.addCriteria(Criteria.where("playerId").in(bo.getPlayerIds()));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GamePlayerPointEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GamePlayerPointVo> page(PageQuery<GamePlayerPointBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GamePlayerPointVo detail(String id) {
        GamePlayerPointEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGamePlayerPoint(GamePlayerPointBo bo) {
        GamePlayerPointEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGamePlayerPoint(GamePlayerPointBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGamePlayerPoint(List<String> ids) {
        return this.deleteByIds(ids);
    }

}