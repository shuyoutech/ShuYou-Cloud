package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameShopBo;
import com.shuyoutech.bbs.domain.entity.GameShopEntity;
import com.shuyoutech.bbs.domain.vo.GameShopVo;
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
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-10 10:02:24
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameShopServiceImpl extends SuperServiceImpl<GameShopEntity, GameShopVo> implements GameShopService {

    @Override
    public List<GameShopVo> convertTo(List<GameShopEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameShopVo convertTo(GameShopEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameShopBo bo) {
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
        GameShopEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameShopVo> page(PageQuery<GameShopBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameShopVo detail(String id) {
        GameShopEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameShop(GameShopBo bo) {
        GameShopEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameShop(GameShopBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameShop(List<String> ids) {
        return this.deleteByIds(ids);
    }

}