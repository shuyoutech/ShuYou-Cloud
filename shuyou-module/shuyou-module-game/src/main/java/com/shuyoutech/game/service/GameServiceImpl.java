package com.shuyoutech.game.service;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.shuyoutech.api.constant.BbsConstants;
import com.shuyoutech.common.core.constant.StringConstants;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapUtils;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.common.web.model.DropDownOptions;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.game.domain.bo.GameBo;
import com.shuyoutech.game.domain.entity.GameAreaServer;
import com.shuyoutech.game.domain.entity.GameEntity;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import com.shuyoutech.game.domain.entity.GameTradingPlatformEntity;
import com.shuyoutech.game.domain.vo.GameGoodsUnitPriceVo;
import com.shuyoutech.game.domain.vo.GameVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-10 09:46:51
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl extends SuperServiceImpl<GameEntity, GameVo> implements GameService {

    @Override
    public List<GameVo> convertTo(List<GameEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameVo convertTo(GameEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameBo bo) {
        Query query = new Query();
        if (null != bo.getHotFlag()) {
            query.addCriteria(Criteria.where("hotFlag").is(bo.getHotFlag()));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameVo> page(PageQuery<GameBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameVo detail(String id) {
        GameEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGame(GameBo bo) {
        GameEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGame(GameBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGame(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public Map<String, Object> querySearch(GameBo bo) {
        Map<String, Object> result = MapUtils.newHashMap();

        String gameId = bo.getId();
        GameEntity game = MongoUtils.getById(gameId, GameEntity.class);
        if (null == game) {
            log.error("querySearch =============== 游戏ID:{} 不存在", gameId);
            return result;
        }
        List<String> goodsTypeList = game.getGoodsTypes();
        result.put("goodsType", goodsTypeList);

        List<String> crossAreaList = game.getCrossAreas();
        result.put("crossArea", crossAreaList);

        List<GameAreaServer> gameAreaList = game.getAreaServers();
        result.put("areaServer", gameAreaList);

        List<String> gameRaceList = game.getGameRaces();
        result.put("gameRace", gameRaceList);

        List<String> gameRoleList = game.getGameRoles();
        result.put("gameRole", gameRoleList);

        List<String> platformIds = game.getPlatformIds();
        if (CollectionUtils.isNotEmpty(platformIds)) {
            List<GameTradingPlatformEntity> platforms = MongoUtils.getByIds(platformIds, GameTradingPlatformEntity.class);
            List<DropDownOptions> platformsList = CollectionUtils.newArrayList();
            if (CollectionUtils.isNotEmpty(platforms)) {
                for (GameTradingPlatformEntity platform : platforms) {
                    platformsList.add(new DropDownOptions().value(platform.getId()).label(platform.getPlatformAlias()).sort(1));
                }
            }
            result.put("gamePlatform", platformsList);
        }
        return result;
    }

    @Override
    public List<GameGoodsUnitPriceVo> latestUnitPrice(String gameId) {
        List<GameGoodsUnitPriceVo> list = CollectionUtils.newArrayList();
        GameEntity game = MongoUtils.getById(gameId, GameEntity.class);
        if (null == game) {
            log.error("latestUnitPrice =============== gameId:{}不存在", gameId);
            return list;
        }
        Map<String, JSONObject> map = RedisUtils.hashEntries(BbsConstants.GAME_GOODS_UNIT_PRICE_KEY + gameId);
        if (MapUtils.isEmpty(map)) {
            log.error("latestUnitPrice =============== redis key:{}无数据", BbsConstants.GAME_GOODS_UNIT_PRICE_KEY + gameId);
            return list;
        }
        String subBefore;
        Map<String, GameGoodsEntity> priceMap = MapUtils.newHashMap();
        for (String key : map.keySet()) {
            GameGoodsEntity gameGoods = JSON.parseObject(JSON.toJSONString(map.get(key)), GameGoodsEntity.class);
            if (null == gameGoods.getUnitPrice()) {
                gameGoods.setUnitPrice(0D);
            }
            subBefore = StringUtils.subBefore(key, StringConstants.HYPHEN, true);
            if (!priceMap.containsKey(subBefore)) {
                priceMap.put(subBefore, gameGoods);
                continue;
            }
            if (priceMap.containsKey(subBefore) //
                    && priceMap.get(subBefore).getUnitPrice() != null && gameGoods.getUnitPrice() != null //
                    && priceMap.get(subBefore).getUnitPrice() < gameGoods.getUnitPrice()) {
                priceMap.put(subBefore, gameGoods);
            }
        }
        GameGoodsUnitPriceVo vo;
        List<GameGoodsEntity> goodsList;
        for (GameAreaServer areaServer : game.getAreaServers()) {
            vo = new GameGoodsUnitPriceVo();
            vo.setAreaServer(areaServer.getGameArea() + StringConstants.HYPHEN + areaServer.getGameServer());
            goodsList = CollectionUtils.newArrayList();
            // 是否有种族分区
            if (CollectionUtils.isEmpty(game.getGameRaces())) {
                if (priceMap.containsKey(vo.getAreaServer())) {
                    goodsList.add(priceMap.get(vo.getAreaServer()));
                } else {
                    GameGoodsEntity goods = new GameGoodsEntity();
                    goods.setUnitPrice(0D);
                    goods.setPriceHint("暂无数据");
                    goodsList.add(goods);
                }
            } else {
                for (String gameRace : game.getGameRaces()) {
                    if (priceMap.containsKey(vo.getAreaServer() + StringConstants.HYPHEN + gameRace)) {
                        goodsList.add(priceMap.get(vo.getAreaServer() + StringConstants.HYPHEN + gameRace));
                    } else {
                        GameGoodsEntity goods = new GameGoodsEntity();
                        goods.setUnitPrice(0D);
                        goods.setPriceHint("暂无数据");
                        goodsList.add(goods);
                    }
                }
            }
            vo.setGoodsList(goodsList);
            list.add(vo);
        }
        return list;
    }

}