package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GameGoodsBo;
import com.shuyoutech.bbs.domain.vo.GameGoodsVo;
import com.shuyoutech.bbs.service.GameGoodsService;
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
 * @date 2025-07-10 11:43:29
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameGoods")
@Tag(name = "GameGoodsController", description = "游戏商品管理API控制器")
public class GameGoodsController {

    @PostMapping("page")
    @Operation(description = "游戏商品分页列表")
    public R<PageResult<GameGoodsVo>> page(@RequestBody PageQuery<GameGoodsBo> pageQuery) {
        return R.success(gameGoodsService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏商品详情")
    public R<GameGoodsVo> detail(@PathVariable String id) {
        return R.success(gameGoodsService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏商品")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameGoodsBo bo) {
        return R.success(gameGoodsService.saveGameGoods(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏商品")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameGoodsBo bo) {
        return R.success(gameGoodsService.updateGameGoods(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏商品")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameGoodsService.deleteGameGoods(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameGoodsService.checkUnique(param));
    }

    private final GameGoodsService gameGoodsService;

}
