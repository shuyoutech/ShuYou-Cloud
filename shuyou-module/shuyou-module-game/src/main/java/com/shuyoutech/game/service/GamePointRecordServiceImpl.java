package com.shuyoutech.game.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.game.domain.bo.GamePointRecordBo;
import com.shuyoutech.game.domain.entity.GamePointRecordEntity;
import com.shuyoutech.game.domain.vo.GamePointRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:49:10
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GamePointRecordServiceImpl extends SuperServiceImpl<GamePointRecordEntity, GamePointRecordVo> implements GamePointRecordService {

    @Override
    public List<GamePointRecordVo> convertTo(List<GamePointRecordEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GamePointRecordVo convertTo(GamePointRecordEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GamePointRecordBo bo) {
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
        GamePointRecordEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GamePointRecordVo> page(PageQuery<GamePointRecordBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GamePointRecordVo detail(String id) {
        GamePointRecordEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGamePointRecord(GamePointRecordBo bo) {
        GamePointRecordEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGamePointRecord(GamePointRecordBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGamePointRecord(List<String> ids) {
        return this.deleteByIds(ids);
    }

}