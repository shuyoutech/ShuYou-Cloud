package com.shuyoutech.crawler.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.crawler.domain.bo.CrawlerGameBo;
import com.shuyoutech.crawler.domain.vo.CrawlerGameVo;
import com.shuyoutech.crawler.service.CrawlerGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("crawlerGame")
@Tag(name = "CrawlerGameController", description = "爬虫游戏管理API控制器")
public class CrawlerGameController {

    @PostMapping("page")
    @Operation(description = "爬虫游戏分页列表")
    public R<PageResult<CrawlerGameVo>> page(@RequestBody PageQuery<CrawlerGameBo> pageQuery) {
        return R.success(crawlerGameService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询爬虫游戏详情")
    public R<CrawlerGameVo> detail(@PathVariable String id) {
        return R.success(crawlerGameService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增爬虫游戏")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody CrawlerGameBo bo) {
        return R.success(crawlerGameService.saveCrawlerGame(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改爬虫游戏")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody CrawlerGameBo bo) {
        return R.success(crawlerGameService.updateCrawlerGame(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除爬虫游戏")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(crawlerGameService.deleteCrawlerGame(ids));
    }

    private final CrawlerGameService crawlerGameService;

}
