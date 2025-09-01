package com.shuyoutech.game.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.game.domain.bo.GameBo;
import com.shuyoutech.game.domain.vo.GameVo;
import com.shuyoutech.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-10 09:46:51
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@Tag(name = "GameController", description = "游戏管理API控制器")
public class GameController {

    @PostMapping("page")
    @Operation(description = "游戏分页列表")
    public R<PageResult<GameVo>> page(@RequestBody PageQuery<GameBo> pageQuery) {
        return R.success(gameService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏详情")
    public R<GameVo> detail(@PathVariable String id) {
        return R.success(gameService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameBo bo) {
        return R.success(gameService.saveGame(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameBo bo) {
        return R.success(gameService.updateGame(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameService.deleteGame(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameService.checkUnique(param));
    }

    @PostMapping("querySearch")
    @Operation(description = "游戏查询搜索条件列表")
    public R<Map<String, Object>> querySearch(@RequestBody GameBo bo) {
        return R.success(gameService.querySearch(bo));
    }

    private final GameService gameService;

}
