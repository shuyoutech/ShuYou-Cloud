package com.shuyoutech.game.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.game.domain.bo.GameLegionBo;
import com.shuyoutech.game.domain.vo.GameLegionVo;
import com.shuyoutech.game.service.GameLegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 12:02:06
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameLegion")
@Tag(name = "GameLegionController", description = "游戏军团管理API控制器")
public class GameLegionController {

    @PostMapping("page")
    @Operation(description = "游戏军团分页列表")
    public R<PageResult<GameLegionVo>> page(@RequestBody PageQuery<GameLegionBo> pageQuery) {
        return R.success(gameLegionService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏军团详情")
    public R<GameLegionVo> detail(@PathVariable String id) {
        return R.success(gameLegionService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏军团")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameLegionBo bo) {
        return R.success(gameLegionService.saveGameLegion(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏军团")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameLegionBo bo) {
        return R.success(gameLegionService.updateGameLegion(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏军团")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameLegionService.deleteGameLegion(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameLegionService.checkUnique(param));
    }

    private final GameLegionService gameLegionService;

}
