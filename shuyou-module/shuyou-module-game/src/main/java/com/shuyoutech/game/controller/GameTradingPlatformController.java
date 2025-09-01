package com.shuyoutech.game.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.game.domain.bo.GameTradingPlatformBo;
import com.shuyoutech.game.domain.vo.GameTradingPlatformVo;
import com.shuyoutech.game.service.GameTradingPlatformService;
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
@RequestMapping("gameTradingPlatform")
@Tag(name = "GameTradingPlatformController", description = "游戏交易平台管理API控制器")
public class GameTradingPlatformController {

    @PostMapping("page")
    @Operation(description = "游戏商城分页列表")
    public R<PageResult<GameTradingPlatformVo>> page(@RequestBody PageQuery<GameTradingPlatformBo> pageQuery) {
        return R.success(gameTradingPlatformService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏商城详情")
    public R<GameTradingPlatformVo> detail(@PathVariable String id) {
        return R.success(gameTradingPlatformService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏商城")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameTradingPlatformBo bo) {
        return R.success(gameTradingPlatformService.saveGameShop(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏商城")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameTradingPlatformBo bo) {
        return R.success(gameTradingPlatformService.updateGameShop(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏商城")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameTradingPlatformService.deleteGameShop(ids));
    }

    private final GameTradingPlatformService gameTradingPlatformService;

}
