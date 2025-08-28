package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GamePointEventBo;
import com.shuyoutech.bbs.domain.entity.GamePointEventEntity;
import com.shuyoutech.bbs.domain.vo.GamePointEventVo;
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
 * @date 2025-07-10 12:39:57
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GamePointEventServiceImpl extends SuperServiceImpl<GamePointEventEntity, GamePointEventVo> implements GamePointEventService {

    @Override
    public List<GamePointEventVo> convertTo(List<GamePointEventEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GamePointEventVo convertTo(GamePointEventEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GamePointEventBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getGuildId())) {
            query.addCriteria(Criteria.where("guildId").is(bo.getGuildId()));
        }
        if (null != bo.getStartDate() && null != bo.getEndDate()) {
            query.addCriteria(Criteria.where("pointDate").gte(bo.getStartDate()).lte(bo.getEndDate()));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GamePointEventEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GamePointEventVo> page(PageQuery<GamePointEventBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GamePointEventVo detail(String id) {
        GamePointEventEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGamePointEvent(GamePointEventBo bo) {
        GamePointEventEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGamePointEvent(GamePointEventBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGamePointEvent(List<String> ids) {
        return this.deleteByIds(ids);
    }

}