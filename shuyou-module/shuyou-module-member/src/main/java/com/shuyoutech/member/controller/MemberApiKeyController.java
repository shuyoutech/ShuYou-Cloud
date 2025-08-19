package com.shuyoutech.member.controller;

import com.shuyoutech.common.core.model.R;
import com.shuyoutech.common.core.model.group.SaveGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.member.domain.bo.MemberApiKeyBo;
import com.shuyoutech.member.domain.vo.MemberApiKeyVo;
import com.shuyoutech.member.service.MemberApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-19 16:57:40
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("apiKey")
@Tag(name = "MemberApiKeyController", description = "用户API key管理API控制器")
public class MemberApiKeyController {

    @PostMapping("list")
    @Operation(description = "用户API key列表")
    public R<List<MemberApiKeyVo>> list(@RequestBody MemberApiKeyBo bo) {
        return R.success(memberApiKeyService.list(bo));
    }

    @PostMapping(path = "detail/{id}")
    @Operation(description = "查询用户API key详情")
    public R<MemberApiKeyVo> detail(@PathVariable String id) {
        return R.success(memberApiKeyService.detail(id));
    }

    @PostMapping(path = "save")
    @Operation(description = "新增用户API key")
    public R<String> save(@Validated({SaveGroup.class}) @RequestBody MemberApiKeyBo bo) {
        return R.success(memberApiKeyService.saveMemberApiKey(bo));
    }

    @PostMapping(path = "update")
    @Operation(description = "修改用户API key")
    public R<Boolean> update(@Validated({UpdateGroup.class}) @RequestBody MemberApiKeyBo bo) {
        return R.success(memberApiKeyService.updateMemberApiKey(bo));
    }

    @PostMapping(path = "delete")
    @Operation(description = "删除用户API key")
    public R<Boolean> delete(@RequestBody List<String> ids) {
        return R.success(memberApiKeyService.deleteMemberApiKey(ids));
    }

    private final MemberApiKeyService memberApiKeyService;

}
