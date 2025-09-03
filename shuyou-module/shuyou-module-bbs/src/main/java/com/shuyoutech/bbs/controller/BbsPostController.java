package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.bo.BbsPostBo;
import com.shuyoutech.bbs.domain.vo.BbsPostVo;
import com.shuyoutech.bbs.service.BbsPostService;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.StatusGroup;
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
 * @date 2025-09-03 13:41:18
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("bbs/post")
@Tag(name = "BbsPostController", description = "帖子管理API控制器")
public class BbsPostController {

    @PostMapping(path = "publish")
    @Operation(description = "发布帖子")
    public R<String> publish(@Validated({SaveGroup.class}) @RequestBody BbsPostBo bo) {
        return R.success(bbsPostService.publishPost(bo));
    }

    @PostMapping("page")
    @Operation(description = "帖子分页列表")
    public R<PageResult<BbsPostVo>> page(@RequestBody PageQuery<BbsPostBo> pageQuery) {
        return R.success(bbsPostService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询帖子详情")
    public R<BbsPostVo> detail(@PathVariable String id) {
        return R.success(bbsPostService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增帖子")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody BbsPostBo bo) {
        return R.success(bbsPostService.saveBbsPost(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改帖子")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody BbsPostBo bo) {
        return R.success(bbsPostService.updateBbsPost(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除帖子")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(bbsPostService.deleteBbsPost(ids));
    }

    @PostMapping(path = "unique")
    @Operation(description = "检测唯一性")
    public R<Boolean> unique(@Validated @RequestBody ParamUnique param) {
        return R.success(bbsPostService.checkUnique(param));
    }

    @PostMapping(path = "status")
    @Operation(description = "状态修改")
    public R<Boolean> status(@Validated({StatusGroup.class}) @RequestBody BbsPostBo bo) {
        return R.success(bbsPostService.statusBbsPost(bo.getId(), bo.getStatus()));
    }

    private final BbsPostService bbsPostService;

}
