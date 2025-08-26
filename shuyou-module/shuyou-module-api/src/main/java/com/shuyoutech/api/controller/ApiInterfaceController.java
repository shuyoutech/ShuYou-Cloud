package com.shuyoutech.api.controller;

import com.shuyoutech.api.domain.bo.ApiInterfaceBo;
import com.shuyoutech.api.domain.vo.ApiInterfaceVo;
import com.shuyoutech.api.service.ApiInterfaceService;
import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("interface")
@Tag(name = "ApiInterfaceController", description = "api接口管理API控制器")
public class ApiInterfaceController {

    @PostMapping("page")
    @Operation(description = "api接口分页列表")
    public R<PageResult<ApiInterfaceVo>> page(@RequestBody PageQuery<ApiInterfaceBo> pageQuery) {
        return R.success(apiInterfaceService.page(pageQuery));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询api接口详情")
    public R<ApiInterfaceVo> detail(@PathVariable String id) {
        return R.success(apiInterfaceService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增api接口")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody ApiInterfaceBo bo) {
        return R.success(apiInterfaceService.saveApi(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改api接口")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody ApiInterfaceBo bo) {
        return R.success(apiInterfaceService.updateApi(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除api接口")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(apiInterfaceService.deleteApi(ids));
    }

    private final ApiInterfaceService apiInterfaceService;

}
