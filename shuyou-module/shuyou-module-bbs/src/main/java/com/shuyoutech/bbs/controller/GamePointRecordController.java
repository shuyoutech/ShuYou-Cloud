package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GamePointRecordBo;
import com.shuyoutech.bbs.domain.vo.GamePointRecordVo;
import com.shuyoutech.bbs.service.GamePointRecordService;
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
 * @date 2025-07-10 12:49:10
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gamePointRecord")
@Tag(name = "GamePointRecordController", description = "游戏积分记录管理API控制器")
public class GamePointRecordController {

    @PostMapping("page")
    @Operation(description = "游戏积分记录分页列表")
    public R<PageResult<GamePointRecordVo>> page(@RequestBody PageQuery<GamePointRecordBo> pageQuery) {
        return R.success(gamePointRecordService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏积分记录详情")
    public R<GamePointRecordVo> detail(@PathVariable String id) {
        return R.success(gamePointRecordService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏积分记录")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GamePointRecordBo bo) {
        return R.success(gamePointRecordService.saveGamePointRecord(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏积分记录")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GamePointRecordBo bo) {
        return R.success(gamePointRecordService.updateGamePointRecord(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏积分记录")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gamePointRecordService.deleteGamePointRecord(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gamePointRecordService.checkUnique(param));
    }

    private final GamePointRecordService gamePointRecordService;

}
