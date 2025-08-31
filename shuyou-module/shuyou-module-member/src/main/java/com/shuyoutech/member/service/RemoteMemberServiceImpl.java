package com.shuyoutech.member.service;

import cn.hutool.core.util.IdUtil;
import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.RemoteMemberUser;
import com.shuyoutech.api.model.SocialUserInfo;
import com.shuyoutech.api.service.RemoteMemberService;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.member.domain.entity.MemberUserEntity;
import com.shuyoutech.member.socail.SocialClientRequest;
import com.shuyoutech.member.socail.SocialClientRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-08-18 13:06
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteMemberServiceImpl implements RemoteMemberService {

    @Override
    public String authorize(String socialType, String callBackSuffix) {
        SocialClientRequest socialClientRequest = socialClientRequestFactory.getAuthRequest(socialType);
        return socialClientRequest.authorize(IdUtil.fastSimpleUUID(), callBackSuffix);
    }

    @Override
    public SocialUserInfo login(AuthAccessToken bo) {
        SocialClientRequest socialClientRequest = socialClientRequestFactory.getAuthRequest(bo.getSocialType());
        return socialClientRequest.login(bo);
    }

    @Override
    public RemoteMemberUser register(String mobile) {
        MemberUserEntity memberUserEntity = memberUserService.register(mobile);
        return MapstructUtils.convert(memberUserEntity, RemoteMemberUser.class);
    }

    @Override
    public RemoteMemberUser register(SocialUserInfo socialUser) {
        MemberUserEntity memberUserEntity = memberUserService.register(socialUser);
        return MapstructUtils.convert(memberUserEntity, RemoteMemberUser.class);
    }

    private final SocialClientRequestFactory socialClientRequestFactory;
    private final MemberUserService memberUserService;

}
