package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GameShopBo;
import com.shuyoutech.bbs.domain.vo.GameShopVo;
import com.shuyoutech.bbs.service.GameShopService;
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
 * @date 2025-07-10 10:02:24
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameShop")
@Tag(name = "GameShopController", description = "游戏商城管理API控制器")
public class GameShopController {

    @PostMapping("page")
    @Operation(description = "游戏商城分页列表")
    public R<PageResult<GameShopVo>> page(@RequestBody PageQuery<GameShopBo> pageQuery) {
        return R.success(gameShopService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏商城详情")
    public R<GameShopVo> detail(@PathVariable String id) {
        return R.success(gameShopService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏商城")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameShopBo bo) {
        return R.success(gameShopService.saveGameShop(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏商城")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameShopBo bo) {
        return R.success(gameShopService.updateGameShop(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏商城")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameShopService.deleteGameShop(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameShopService.checkUnique(param));
    }

    private final GameShopService gameShopService;

}
