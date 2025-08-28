package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GameItemBo;
import com.shuyoutech.bbs.domain.vo.GameItemVo;
import com.shuyoutech.bbs.service.GameItemService;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 10:34:21
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameItem")
@Tag(name = "GameItemController", description = "游戏物品管理API控制器")
public class GameItemController {

    @PostMapping("page")
    @Operation(description = "游戏物品分页列表")
    public R<PageResult<GameItemVo>> page(@RequestBody PageQuery<GameItemBo> pageQuery) {
        return R.success(gameItemService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏物品详情")
    public R<GameItemVo> detail(@PathVariable String id) {
        return R.success(gameItemService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏物品")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameItemBo bo) {
        return R.success(gameItemService.saveGameItem(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏物品")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameItemBo bo) {
        return R.success(gameItemService.updateGameItem(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏物品")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameItemService.deleteGameItem(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameItemService.checkUnique(param));
    }

    private final GameItemService gameItemService;

}
