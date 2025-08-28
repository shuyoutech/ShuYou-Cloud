package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameItemBo;
import com.shuyoutech.bbs.domain.entity.GameItemEntity;
import com.shuyoutech.bbs.domain.entity.GameItemMaterial;
import com.shuyoutech.bbs.domain.vo.GameItemVo;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-10 10:34:21
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameItemServiceImpl extends SuperServiceImpl<GameItemEntity, GameItemVo> implements GameItemService {

    @Override
    public List<GameItemVo> convertTo(List<GameItemEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return CollectionUtils.newArrayList();
        }
        List<GameItemVo> itemVoList = MapstructUtils.convert(list, this.voClass);
        if (CollectionUtils.isEmpty(itemVoList)) {
            return CollectionUtils.newArrayList();
        }
        for (GameItemVo gameItemEntity : itemVoList) {
            calPrice(gameItemEntity);
            gameItemEntity.setItemCostProfit(gameItemEntity.getItemSellPrice().subtract(gameItemEntity.getItemCostPrice()));
            gameItemEntity.setItemBuyProfit(gameItemEntity.getItemSellPrice().subtract(gameItemEntity.getItemBuyPrice()));
        }
        return itemVoList;
    }

    public GameItemVo convertTo(GameItemEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }


    private void calPrice(GameItemVo item) {
        List<GameItemMaterial> materialList = item.getItemMaterialList();
        if (CollectionUtils.isEmpty(materialList)) {
            return;
        }
        List<String> itemIdList = StreamUtils.toList(materialList, GameItemMaterial::getGameItemId);
        Map<String, GameItemEntity> itemMap = getByIds(itemIdList, GameItemEntity::getId);
        BigDecimal itemCostPrice = BigDecimal.ZERO;
        GameItemEntity itemEntity;
        GameItemVo vo;
        for (GameItemMaterial material : materialList) {
            itemEntity = itemMap.get(material.getGameItemId());
            if (null == itemEntity) {
                continue;
            }
            vo = MapstructUtils.convert(itemEntity, this.voClass);
            if (null == vo) {
                continue;
            }
            calPrice(vo);
            itemCostPrice = itemCostPrice.add(NumberUtils.mul(material.getNum(), itemEntity.getItemCostPrice()));
            material.setGameItemName(itemEntity.getItemName());
        }
        item.setItemCostPrice(NumberUtils.round(itemCostPrice, item.getPriceScale()));
    }

    @Override
    public Query buildQuery(GameItemBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getItemName())) {
            query.addCriteria(Criteria.where("itemName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getItemName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameItemEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameItemVo> page(PageQuery<GameItemBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameItemVo detail(String id) {
        GameItemEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameItem(GameItemBo bo) {
        GameItemEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameItem(GameItemBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameItem(List<String> ids) {
        return this.deleteByIds(ids);
    }

}