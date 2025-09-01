package com.shuyoutech.game.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.game.domain.bo.GameGoodsBo;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import com.shuyoutech.game.domain.vo.GameGoodsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 11:43:29
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameGoodsServiceImpl extends SuperServiceImpl<GameGoodsEntity, GameGoodsVo> implements GameGoodsService {

    @Override
    public List<GameGoodsVo> convertTo(List<GameGoodsEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameGoodsVo convertTo(GameGoodsEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameGoodsBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameGoodsEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameGoodsVo> page(PageQuery<GameGoodsBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameGoodsVo detail(String id) {
        GameGoodsEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameGoods(GameGoodsBo bo) {
        GameGoodsEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameGoods(GameGoodsBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameGoods(List<String> ids) {
        return this.deleteByIds(ids);
    }

}