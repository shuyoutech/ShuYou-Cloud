package com.shuyoutech.game.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.DateUtils;
import com.shuyoutech.common.core.util.StreamUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.game.domain.bo.GamePlayerBo;
import com.shuyoutech.game.domain.bo.GamePlayerPointBo;
import com.shuyoutech.game.domain.bo.GamePointEventBo;
import com.shuyoutech.game.domain.bo.GamePointRecordBo;
import com.shuyoutech.game.domain.entity.GamePlayerEntity;
import com.shuyoutech.game.domain.entity.GamePointEventEntity;
import com.shuyoutech.game.domain.entity.GamePointRecordEntity;
import com.shuyoutech.game.domain.vo.GamePlayerPointVo;
import com.shuyoutech.game.service.GamePlayerPointService;
import com.shuyoutech.game.service.GamePlayerService;
import com.shuyoutech.game.service.GamePointEventService;
import com.shuyoutech.game.service.GamePointRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YangChao
 * @date 2025-07-10 12:43:36
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gamePlayerPoint")
@Tag(name = "GamePlayerPointController", description = "游戏玩家积分管理API控制器")
public class GamePlayerPointController {

    @PostMapping("page")
    @Operation(description = "游戏玩家积分分页列表")
    public R<PageResult<GamePlayerPointVo>> page(@RequestBody PageQuery<GamePlayerPointBo> pageQuery) {
        return R.success(gamePlayerPointService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏玩家积分详情")
    public R<GamePlayerPointVo> detail(@PathVariable String id) {
        return R.success(gamePlayerPointService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏玩家积分")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GamePlayerPointBo bo) {
        return R.success(gamePlayerPointService.saveGamePlayerPoint(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏玩家积分")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GamePlayerPointBo bo) {
        return R.success(gamePlayerPointService.updateGamePlayerPoint(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏玩家积分")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gamePlayerPointService.deleteGamePlayerPoint(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gamePlayerPointService.checkUnique(param));
    }

    @PostMapping("summaryByMonth")
    @Operation(description = "按月汇总查询")
    public R<?> summaryByMonth(@RequestBody GamePlayerBo bo) {
        GamePlayerBo playerBo = new GamePlayerBo();
        playerBo.setGuildId(bo.getGuildId());
        playerBo.setPlayerName(bo.getPlayerName());
        List<GamePlayerEntity> players = gamePlayerService.selectList(gamePlayerService.buildQuery(playerBo));
        Map<String, BigDecimal> pointMap = gamePlayerService.calPlayerPointMonth(bo.getGuildId(), bo.getMonth());
        List<String> dayList = DateUtils.dayByMonth(bo.getMonth());
        List<Object> list = CollectionUtil.newArrayList();
        List<Object> temp;
        for (GamePlayerEntity player : players) {
            temp = CollectionUtil.newArrayList();
            temp.add(player.getGuildName() + "-" + "-" + player.getPlayerName());
            for (String day : dayList) {
                temp.add(pointMap.getOrDefault(player.getId() + "_" + day, BigDecimal.ZERO));
            }
            list.add(temp);
        }
        JSONObject object = new JSONObject();
        object.put("dayList", dayList.stream().map(m -> m.substring(8)).collect(Collectors.toList()));
        object.put("dataList", list);
        return R.success(object);
    }

    @GetMapping("rankByMonth")
    @Operation(description = "按月汇总查询")
    public R<?> rankByMonth(@RequestBody GamePlayerBo bo) {
        GamePlayerBo playerBo = new GamePlayerBo();
        playerBo.setGuildId(bo.getGuildId());
        playerBo.setPlayerName(bo.getPlayerName());
        List<GamePlayerEntity> players = gamePlayerService.selectList(gamePlayerService.buildQuery(playerBo));
        if (CollectionUtils.isEmpty(players)) {
            return R.success();
        }
        Map<String, GamePlayerEntity> playerMap = StreamUtils.toIdentityMap(players, GamePlayerEntity::getId);

        DateTime monthDate = DateUtil.parse(bo.getMonth(), "yyyy-MM");
        DateTime beginMonth = DateUtil.beginOfMonth(monthDate);
        DateTime endMonth = DateUtil.endOfMonth(monthDate);
        Date startDate = DateUtil.parseDateTime(beginMonth.toDateStr() + " 00:00:00").toJdkDate();
        Date endDate = DateUtil.parseDateTime(endMonth.toDateStr() + " 23:59:59").toJdkDate();

        GamePointEventBo pointEventBo = new GamePointEventBo();
        pointEventBo.setGuildId(bo.getGuildId());
        pointEventBo.setStartDate(startDate);
        pointEventBo.setEndDate(endDate);
        List<GamePointEventEntity> pointEventList = gamePointEventService.selectList(gamePointEventService.buildQuery(pointEventBo));
        if (CollectionUtils.isEmpty(pointEventList)) {
            return R.success();
        }

        GamePointRecordBo pointRecordBo = new GamePointRecordBo();
        pointRecordBo.setGuildId(bo.getGuildId());
        pointRecordBo.setStartDate(startDate);
        pointRecordBo.setEndDate(endDate);
        List<GamePointRecordEntity> pointRecords = gamePointRecordService.selectList(gamePointRecordService.buildQuery(pointRecordBo));
        if (CollectionUtils.isEmpty(pointRecords)) {
            return R.success();
        }

        Map<String, List<GamePointRecordEntity>> playerPointMap = StreamUtils.groupByKey(pointRecords, GamePointRecordEntity::getPlayerId);
        List<List<Object>> list = CollectionUtil.newArrayList();
        List<Object> temp;
        GamePlayerEntity player;
        int count;
        for (String playerId : playerPointMap.keySet()) {
            player = playerMap.get(playerId);
            if (null == player) {
                continue;
            }
            count = playerPointMap.get(playerId).size();
            temp = CollectionUtil.newArrayList();
            temp.add(player.getGuildName() + "-" + player.getPlayerName());
            temp.add(count);
            temp.add(pointRecords.size());
            temp.add(NumberUtil.formatPercent(NumberUtil.div(count, pointRecords.size(), 2), 0));
            list.add(temp);
        }

        CollectionUtil.sort(list, (o1, o2) -> NumberUtil.compare(Integer.parseInt(o2.get(1).toString()), Integer.parseInt(o1.get(1).toString())));
        int rank = 1;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.get(i).add(rank);
                rank++;
            } else {
                if (Integer.parseInt(list.get(i - 1).get(1).toString()) == Integer.parseInt(list.get(i).get(1).toString())) {
                    list.get(i).add(list.get(i - 1).get(4));
                } else {
                    list.get(i).add(rank);
                    rank++;
                }
            }
        }
        return R.success(list);
    }

    private final GamePlayerPointService gamePlayerPointService;
    private final GamePlayerService gamePlayerService;
    private final GamePointRecordService gamePointRecordService;
    private final GamePointEventService gamePointEventService;

}
