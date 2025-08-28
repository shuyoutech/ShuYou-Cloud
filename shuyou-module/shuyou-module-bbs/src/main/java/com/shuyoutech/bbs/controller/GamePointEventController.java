package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GamePointEventBo;
import com.shuyoutech.bbs.domain.vo.GamePointEventVo;
import com.shuyoutech.bbs.service.GamePointEventService;
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
 * @date 2025-07-10 12:39:57
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gamePointEvent")
@Tag(name = "GamePointEventController", description = "游戏积分活动管理API控制器")
public class GamePointEventController {

    @PostMapping("page")
    @Operation(description = "游戏积分活动分页列表")
    public R<PageResult<GamePointEventVo>> page(@RequestBody PageQuery<GamePointEventBo> pageQuery) {
        return R.success(gamePointEventService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏积分活动详情")
    public R<GamePointEventVo> detail(@PathVariable String id) {
        return R.success(gamePointEventService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏积分活动")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GamePointEventBo bo) {
        return R.success(gamePointEventService.saveGamePointEvent(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏积分活动")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GamePointEventBo bo) {
        return R.success(gamePointEventService.updateGamePointEvent(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏积分活动")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gamePointEventService.deleteGamePointEvent(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gamePointEventService.checkUnique(param));
    }

    private final GamePointEventService gamePointEventService;

}
