package com.shuyoutech.auth.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-02-14 10:24
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "SocialVerifyController", description = "社交服务校验API控制器")
public class SocialVerifyController {

    @GetMapping(produces = "text/plain;charset=utf-8", path = "/wechatOpen/verify")
    @Operation(summary = "微信回调地址验证", description = "微信回调地址验证")
    public Long wechatOpenVerify(@RequestParam(name = "signature", required = false) String signature, //
                                 @RequestParam(name = "timestamp", required = false) String timestamp,  //
                                 @RequestParam(name = "nonce", required = false) String nonce,  //
                                 @RequestParam(name = "echostr", required = false) String echostr) {
        // https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
        log.info("wechatOpenVerify ======= signature: {}, timestamp: {}, nonce: {}, echo: {}", signature, timestamp, nonce, echostr);
        List<String> arrList = CollectionUtils.sortByPinyin(Arrays.asList("123456", timestamp, nonce));
        String sha1Hex = DigestUtil.sha1Hex(CollectionUtils.join(arrList, ""));
        if (StringUtils.equalsIgnoreCase(signature, sha1Hex)) {
            return Long.valueOf(echostr);
        }
        return null;
    }

}
