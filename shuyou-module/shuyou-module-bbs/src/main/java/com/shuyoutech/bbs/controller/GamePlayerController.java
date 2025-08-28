package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GamePlayerBo;
import com.shuyoutech.bbs.domain.bo.GamePlayerPointRegisterBo;
import com.shuyoutech.bbs.domain.bo.GamePlayerPointSettleBo;
import com.shuyoutech.bbs.domain.vo.GamePlayerVo;
import com.shuyoutech.bbs.service.GamePlayerService;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.model.TreeOption;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author YangChao
 * @date 2025-07-10 12:32:24
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gamePlayer")
@Tag(name = "GamePlayerController", description = "游戏玩家管理API控制器")
public class GamePlayerController {

    @PostMapping("page")
    @Operation(description = "游戏玩家分页列表")
    public R<PageResult<GamePlayerVo>> page(@RequestBody PageQuery<GamePlayerBo> pageQuery) {
        return R.success(gamePlayerService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏玩家详情")
    public R<GamePlayerVo> detail(@PathVariable String id) {
        return R.success(gamePlayerService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏玩家")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GamePlayerBo bo) {
        return R.success(gamePlayerService.saveGamePlayer(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏玩家")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GamePlayerBo bo) {
        return R.success(gamePlayerService.updateGamePlayer(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏玩家")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gamePlayerService.deleteGamePlayer(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gamePlayerService.checkUnique(param));
    }

    @PostMapping(path = "tree")
    @Operation(description = "玩家树")
    public R<List<TreeOption>> tree() {
        return R.success(gamePlayerService.tree());
    }

    @PostMapping(path = "matchPlayer")
    @Operation(description = "提取图片文字匹配玩家")
    public R<Set<String>> matchPlayer(@RequestBody List<String> eventFileIds) {
        return R.success(gamePlayerService.matchPlayer(eventFileIds));
    }

    @PostMapping(path = "pointRegister")
    @Operation(description = "玩家积分登记")
    public R<Void> pointRegister(@RequestBody GamePlayerPointRegisterBo bo) {
        gamePlayerService.pointRegister(bo);
        return R.success();
    }

    @PostMapping(path = "pointSettle")
    @Operation(description = "玩家积分清算")
    public R<Void> pointSettle(@RequestBody GamePlayerPointSettleBo bo) {
        gamePlayerService.pointSettle(bo);
        return R.success();
    }

    private final GamePlayerService gamePlayerService;

}
