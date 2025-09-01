package com.shuyoutech.game.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.game.domain.bo.GameTradingPlatformBo;
import com.shuyoutech.game.domain.entity.GameTradingPlatformEntity;
import com.shuyoutech.game.domain.vo.GameTradingPlatformVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameTradingPlatformServiceImpl extends SuperServiceImpl<GameTradingPlatformEntity, GameTradingPlatformVo> implements GameTradingPlatformService {

    @Override
    public List<GameTradingPlatformVo> convertTo(List<GameTradingPlatformEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameTradingPlatformVo convertTo(GameTradingPlatformEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameTradingPlatformBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getShopName())) {
            query.addCriteria(Criteria.where("shopName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getShopName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameTradingPlatformEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameTradingPlatformVo> page(PageQuery<GameTradingPlatformBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameTradingPlatformVo detail(String id) {
        GameTradingPlatformEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameShop(GameTradingPlatformBo bo) {
        GameTradingPlatformEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameShop(GameTradingPlatformBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameShop(List<String> ids) {
        return this.deleteByIds(ids);
    }

}