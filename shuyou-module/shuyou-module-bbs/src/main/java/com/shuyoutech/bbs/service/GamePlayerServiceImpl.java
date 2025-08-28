package com.shuyoutech.bbs.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.bbs.domain.bo.*;
import com.shuyoutech.bbs.domain.entity.*;
import com.shuyoutech.bbs.domain.vo.GamePlayerVo;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.model.TreeOption;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GamePlayerServiceImpl extends SuperServiceImpl<GamePlayerEntity, GamePlayerVo> implements GamePlayerService {

    @Override
    public List<GamePlayerVo> convertTo(List<GamePlayerEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GamePlayerVo convertTo(GamePlayerEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GamePlayerBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getGuildId())) {
            query.addCriteria(Criteria.where("guildId").is(bo.getGuildId()));
        }
        if (StringUtils.isNotBlank(bo.getPlayerName())) {
            query.addCriteria(Criteria.where("playerName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getPlayerName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GamePlayerEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GamePlayerVo> page(PageQuery<GamePlayerBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GamePlayerVo detail(String id) {
        GamePlayerEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGamePlayer(GamePlayerBo bo) {
        GamePlayerEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGamePlayer(GamePlayerBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGamePlayer(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public List<TreeOption> tree() {
        List<TreeOption> result = CollectionUtils.newArrayList();
        String userId = AuthUtils.getLoginUserId();
        // todo 待完善
        String guildId = "";
        GameGuildEntity guild = gameGuildService.getById(guildId);
        if (null == guild) {
            log.error("tree =================== guildId:{} 公会不存在", guildId);
            return result;
        }
        GameLegionBo legionBo = new GameLegionBo();
        legionBo.setGuildId(guild.getId());
        List<GameLegionEntity> legions = gameLegionService.selectList(gameLegionService.buildQuery(legionBo));
        if (CollectionUtil.isEmpty(legions)) {
            log.error("tree =================== guildId:{} 公会没有关联军团", guildId);
            return result;
        }
        GameEntity game;
        TreeOption legionParent;
        List<TreeOption> gameRoleList;
        List<GameCharactersEntity> characters;
        List<TreeOption> childList;
        for (GameLegionEntity legion : legions) {
            game = gameService.getById(legion.getGameId());
            if (null == game) {
                continue;
            }
            gameRoleList = CollectionUtil.newArrayList();
            for (String role : game.getGameRoleList()) {
                gameRoleList.add(TreeOption.builder().parentId(legion.getId()).label(role).value(role).type("gameRole").build());
            }
            GameCharactersBo charactersBo = new GameCharactersBo();
            charactersBo.setLegionId(legion.getId());
            characters = gameCharactersService.selectList(gameCharactersService.buildQuery(charactersBo));
            if (CollectionUtil.isEmpty(characters)) {
                continue;
            }
            Map<String, List<GameCharactersEntity>> roleMap = StreamUtils.groupByKey(characters, GameCharactersEntity::getGameRole);
            for (TreeOption gameRole : gameRoleList) {
                childList = CollectionUtil.newArrayList();
                if (roleMap.containsKey(gameRole.getLabel())) {
                    for (GameCharactersEntity gameCharacters : roleMap.get(gameRole.getLabel())) {
                        childList.add(TreeOption.builder().label(gameCharacters.getCharactersName()).value(gameCharacters.getId()).type("gameCharacters").build());
                    }
                }
                gameRole.setChildren(childList);
            }
            legionParent = TreeOption.builder().build();
            legionParent.setParentId("0");
            legionParent.setLabel(legion.getLegionName());
            legionParent.setValue(legion.getId());
            legionParent.setType("legion");
            legionParent.setChildren(gameRoleList);
            result.add(legionParent);
        }
        return result;
    }

    @Override
    public void pointRegister(GamePlayerPointRegisterBo bo) {
        String loginUserId = AuthUtils.getLoginUserId();
        Set<String> charactersIds = bo.getCharactersIds();
        // todo 待完善
        String guildId = "";
        String eventId = IdUtil.getSnowflakeNextIdStr();
        GamePointEventBo pointEvent = new GamePointEventBo();
        pointEvent.setId(eventId);
        pointEvent.setEventDate(bo.getEventDate());
        pointEvent.setEventName(bo.getEventName());
        pointEvent.setEventPoint(bo.getEventPoint());
        pointEvent.setGuildId(guildId);
        gamePointEventService.save(pointEvent);

        GamePlayerBo playerBo = new GamePlayerBo();
        playerBo.setGuildId(guildId);
        List<GamePlayerEntity> playerList = this.selectList(this.buildQuery(playerBo));
        if (CollectionUtil.isEmpty(playerList)) {
            return;
        }
        Map<String, GamePlayerEntity> playerCharactersMap = MapUtils.newHashMap();
        Map<String, GamePlayerEntity> playerMap = MapUtils.newHashMap();
        for (GamePlayerEntity player : playerList) {
            for (String charactersId : charactersIds) {
                playerCharactersMap.put(charactersId, player);
            }
            playerMap.put(player.getId(), player);
        }
        List<GameCharactersEntity> charactersList = gameCharactersService.getByIds(charactersIds);
        Map<String, GameCharactersEntity> charactersMap = StreamUtils.toIdentityMap(charactersList, GameCharactersEntity::getId);

        List<GamePointRecordEntity> records = CollectionUtils.newArrayList();
        GamePointRecordEntity record;
        GameCharactersEntity characters;
        GamePlayerEntity player;
        Set<String> playerIds = CollectionUtil.newHashSet();
        for (String charactersId : charactersIds) {
            if (!charactersMap.containsKey(charactersId) || !playerCharactersMap.containsKey(charactersId)) {
                continue;
            }
            characters = charactersMap.get(charactersId);
            player = playerCharactersMap.get(charactersId);
            record = new GamePointRecordEntity();
            record.setPlayerId(player.getId());
            record.setPlayerName(player.getPlayerName());
            record.setGuildId(characters.getGuildId());
            record.setGuildName(characters.getGuildName());
            record.setLegionId(characters.getLegionId());
            record.setLegionName(characters.getLegionName());
            record.setCharactersId(characters.getId());
            record.setCharactersName(characters.getCharactersName());
            record.setEventId(eventId);
            record.setEventName(bo.getEventName());
            record.setPointDate(bo.getEventDate());
            record.setPoint(bo.getEventPoint());
            records.add(record);

            playerIds.add(player.getId());
        }
        gamePointRecordService.saveBatch(records);

        Map<String, BigDecimal> playerPointMap = this.calPlayerPoint(playerIds, 0, 0);
        List<GamePlayerPointEntity> playerPoints = gamePlayerPointService.getByIds(playerIds);
        Map<String, GamePlayerPointEntity> pointMap = StreamUtils.toIdentityMap(playerPoints, GamePlayerPointEntity::getPlayerId);
        List<GamePlayerPointEntity> insertList = CollectionUtil.newArrayList();
        List<GamePlayerPointEntity> updateList = CollectionUtil.newArrayList();
        GamePlayerPointEntity playerPoint;
        for (String playerId : playerIds) {
            if (pointMap.containsKey(playerId)) {
                playerPoint = pointMap.get(playerId);
                playerPoint.setTotalPoint(playerPointMap.getOrDefault(playerPoint.getPlayerId(), BigDecimal.ZERO));
                playerPoint.setAvailablePoint(playerPoint.getTotalPoint().subtract(playerPoint.getUsePoint()));
                updateList.add(playerPoint);
            } else {
                playerPoint = new GamePlayerPointEntity();
                playerPoint.setId(IdUtil.getSnowflakeNextIdStr());
                playerPoint.setGuildId(playerMap.get(playerId).getGuildId());
                playerPoint.setGuildName(playerMap.get(playerId).getGuildName());
                playerPoint.setPlayerId(playerId);
                playerPoint.setPlayerName(playerMap.get(playerId).getPlayerName());
                playerPoint.setUsePoint(BigDecimal.ZERO);
                playerPoint.setTotalPoint(playerPointMap.getOrDefault(playerPoint.getPlayerId(), BigDecimal.ZERO));
                playerPoint.setAvailablePoint(playerPoint.getTotalPoint());
                insertList.add(playerPoint);
            }
        }
        gamePlayerPointService.saveBatch(insertList);
        gamePlayerPointService.updateBatch(updateList);
    }

    @Override
    public void pointSettle(GamePlayerPointSettleBo bo) {
        GamePlayerPointBo playerPointBo = new GamePlayerPointBo();
        playerPointBo.setPlayerIds(bo.getPlayerIds());
        List<GamePlayerPointEntity> playerPoints = gamePlayerPointService.selectList(gamePlayerPointService.buildQuery(playerPointBo));
        if (CollectionUtils.isEmpty(playerPoints)) {
            return;
        }
        List<GamePlayerPointEntity> updateList = CollectionUtil.newArrayList();
        GamePlayerPointEntity update;
        for (GamePlayerPointEntity playerPoint : playerPoints) {
            update = new GamePlayerPointEntity();
            update.setId(playerPoint.getId());
            update.setAvailablePoint(playerPoint.getAvailablePoint().subtract(bo.getPoint()));
            update.setUsePoint(playerPoint.getUsePoint().add(bo.getPoint()));
            updateList.add(update);
        }
        gamePlayerPointService.patchBatch(updateList);
    }

    @Override
    public Map<String, BigDecimal> calPlayerPoint(Set<String> playerIds, long start, long end) {
        Map<String, BigDecimal> playerMap = MapUtil.newHashMap();
        try {
            MatchOperation matchOperation;
            if (start > 0 && end > 0) {
                matchOperation = Aggregation.match(Criteria.where("playerId").in(playerIds).and("pointDate").gte(new Date(start)).lte(new Date(end)));
            } else {
                matchOperation = Aggregation.match(Criteria.where("playerId").in(playerIds));
            }
            ProjectionOperation projectionOperation = Aggregation.project("playerId", "point");
            GroupOperation groupOperation = Aggregation.group("playerId").sum("point").as("pointSum");
            ProjectionOperation previousOperation = Aggregation.project("playerId", "pointSum").and("playerId").previousOperation();
            Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation, groupOperation, previousOperation);
            List<Document> documents = MongoUtils.mongoTemplate.aggregate(aggregation, GamePointRecordEntity.class, Document.class).getMappedResults();
            for (Document document : documents) {
                playerMap.put(document.getString("playerId"), document.get("pointSum", Decimal128.class).bigDecimalValue());
            }
        } catch (Exception e) {
            log.error("calPlayerPoint ======================== {}", e.getMessage());
        }
        return playerMap;
    }

    @Override
    public Map<String, BigDecimal> calPlayerPointMonth(String guildId, String month) {
        Map<String, BigDecimal> playerMap = MapUtil.newHashMap();
        try {
            DateTime monthDate = DateUtil.parse(month, "yyyy-MM");
            DateTime beginMonth = DateUtil.beginOfMonth(monthDate);
            DateTime endMonth = DateUtil.endOfMonth(monthDate);
            Date startDate = DateUtil.parseDateTime(beginMonth.toDateStr() + " 00:00:00").toJdkDate();
            Date endDate = DateUtil.parseDateTime(endMonth.toDateStr() + " 23:59:59").toJdkDate();
            MatchOperation matchOperation = Aggregation.match(Criteria.where("guildId").is(guildId).and("pointDate").gte(startDate).lte(endDate));
            ProjectionOperation projectionOperation = Aggregation.project("playerId", "pointDate", "point").andExpression("{$dateToString:{ format:'%Y-%m-%d',date: '$pointDate', timezone: 'Asia/Shanghai'}}").as("pointDate");
            GroupOperation groupOperation = Aggregation.group("playerId", "pointDate").sum("point").as("pointSum");
            Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation, groupOperation);
            List<Document> documents = MongoUtils.mongoTemplate.aggregate(aggregation, GamePointRecordEntity.class, Document.class).getMappedResults();
            for (Document document : documents) {
                playerMap.put(document.get("_id", Document.class).getString("playerId") + "_" + document.get("_id", Document.class).getString("pointDate"), document.get("pointSum", Decimal128.class).bigDecimalValue());
            }
        } catch (Exception e) {
            log.error("calPlayerPointMonth ======================== {}", e.getMessage());
        }
        return playerMap;
    }

    @Override
    public Set<String> matchPlayer(List<String> eventFileIds) {
        Set<String> list = CollectionUtils.newHashSet();
        String loginUserId = AuthUtils.getLoginUserId();
        // todo 待完善
        String guildId = "";
        GameCharactersBo playerBo = new GameCharactersBo();
        playerBo.setGuildId(guildId);
        List<GameCharactersEntity> playerList = gameCharactersService.selectList(gameCharactersService.buildQuery(playerBo));
        if (CollectionUtil.isEmpty(playerList)) {
            return list;
        }
        String fileUrl;
        JSONObject object;
        String content;
        for (String eventFileId : eventFileIds) {
           /* fileUrl = remoteFileService.generatedUrl(eventFileId, 1800000L);
            object = OcrUtils.recognizeGeneral(fileUrl);
            if (null == object) {
                continue;
            }
            content = object.getString("content");
            if (StringUtils.isEmpty(content)) {
                continue;
            }
            for (GameCharactersEntity player : playerList) {
                if (content.contains(player.getCharactersName())) {
                    list.add(player.getId());
                }
            }*/
        }
        return list;
    }

    private final GameService gameService;
    private final GameGuildService gameGuildService;
    private final GameLegionService gameLegionService;
    private final GameCharactersService gameCharactersService;
    private final GamePointEventService gamePointEventService;
    private final GamePointRecordService gamePointRecordService;
    private final GamePlayerPointService gamePlayerPointService;

}