package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.GameCharactersBo;
import com.shuyoutech.bbs.domain.vo.GameCharactersVo;
import com.shuyoutech.bbs.service.GameCharactersService;
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
 * @date 2025-07-10 12:13:36
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("gameCharacters")
@Tag(name = "GameCharactersController", description = "游戏角色管理API控制器")
public class GameCharactersController {

    @PostMapping("page")
    @Operation(description = "游戏角色分页列表")
    public R<PageResult<GameCharactersVo>> page(@RequestBody PageQuery<GameCharactersBo> pageQuery) {
        return R.success(gameCharactersService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询游戏角色详情")
    public R<GameCharactersVo> detail(@PathVariable String id) {
        return R.success(gameCharactersService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增游戏角色")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody GameCharactersBo bo) {
        return R.success(gameCharactersService.saveGameCharacters(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改游戏角色")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody GameCharactersBo bo) {
        return R.success(gameCharactersService.updateGameCharacters(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除游戏角色")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(gameCharactersService.deleteGameCharacters(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(gameCharactersService.checkUnique(param));
    }

    private final GameCharactersService gameCharactersService;

}
