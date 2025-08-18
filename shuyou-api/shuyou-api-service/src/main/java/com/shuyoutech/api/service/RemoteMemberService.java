package com.shuyoutech.api.service;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.RemoteMemberUser;
import com.shuyoutech.api.model.SocialUserInfo;

/**
 * @author YangChao
 * @date 2025-08-18 13:04
 **/
public interface RemoteMemberService {

    String authorize(String socialType);

    SocialUserInfo login(AuthAccessToken bo);

    RemoteMemberUser register(String mobile);

    RemoteMemberUser register(SocialUserInfo socialUser);

}
