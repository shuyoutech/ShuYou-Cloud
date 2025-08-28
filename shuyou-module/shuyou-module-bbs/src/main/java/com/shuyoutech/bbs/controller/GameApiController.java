package com.shuyoutech.bbs.controller;

import com.shuyoutech.bbs.domain.vo.GameGoodsUnitPriceVo;
import com.shuyoutech.bbs.service.GameService;
import com.shuyoutech.common.core.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-10 11:32
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/game")
@Tag(name = "GameApiController", description = "游戏无权限API控制器")
public class GameApiController {

    @PostMapping("latestUnitPrice/{gameId}")
    @Operation(description = "最新游戏币价格")
    public R<List<GameGoodsUnitPriceVo>> latestUnitPrice(@PathVariable String gameId) {
        return R.success(gameService.latestUnitPrice(gameId));
    }

    private final GameService gameService;

}
