package com.shuyoutech.game.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.game.domain.bo.GameGuildBo;
import com.shuyoutech.game.domain.vo.GameGuildVo;
import com.shuyoutech.game.service.GameGuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 11:52:43
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameGuild")
@Tag(name = "GameGuildController", description = "游戏公会管理API控制器")
public class GameGuildController {

    @PostMapping("page")
    @Operation(description = "游戏公会分页列表")
    public R<PageResult<GameGuildVo>> page(@RequestBody PageQuery<GameGuildBo> pageQuery) {
        return R.success(gameGuildService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏公会详情")
    public R<GameGuildVo> detail(@PathVariable String id) {
        return R.success(gameGuildService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏公会")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameGuildBo bo) {
        return R.success(gameGuildService.saveGameGuild(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏公会")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameGuildBo bo) {
        return R.success(gameGuildService.updateGameGuild(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏公会")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameGuildService.deleteGameGuild(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameGuildService.checkUnique(param));
    }

    @PostMapping(path = "status")
    @Operation(description = "状态修改")
    public R<Boolean> status(@Validated({StatusGroup.class}) @RequestBody GameGuildBo bo) {
        return R.success(gameGuildService.statusGameGuild(bo.getId(), bo.getStatus()));
    }

    private final GameGuildService gameGuildService;

}
