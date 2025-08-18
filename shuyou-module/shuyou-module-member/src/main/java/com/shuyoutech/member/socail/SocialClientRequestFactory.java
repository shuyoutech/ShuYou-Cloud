package com.shuyoutech.member.socail;

import com.shuyoutech.api.enums.SocialTypeEnum;
import com.shuyoutech.api.model.RemoteSocialClient;
import com.shuyoutech.api.service.RemoteMemberService;
import com.shuyoutech.common.core.enums.ErrorCodeEnum;
import com.shuyoutech.common.core.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-07-10 10:47
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialClientRequestFactory {

    public SocialClientRequest getAuthRequest(String socialType) {
        RemoteSocialClient socialClient = remoteMemberService.socialClientBySocialType(socialType);
        if (null == socialClient) {
            throw new AuthException(ErrorCodeEnum.PARAM_ERROR);
        }
        SocialClientConfig config = SocialClientConfig.builder() //
                .clientId(socialClient.getClientId()) //
                .clientSecret(socialClient.getClientSecret()) //
                .redirectUri(socialClient.getRedirectUri()) //
                .build();
        if (SocialTypeEnum.WECHAT_OPEN.getValue().equalsIgnoreCase(socialType)) {
            return new SocialClientWechatOpenRequest(config);
        } else if (SocialTypeEnum.WECHAT_MINI_PROGRAM.getValue().equalsIgnoreCase(socialType)) {
            return new SocialClientWechatMiniProgramRequest(config);
        }
        throw new AuthException(ErrorCodeEnum.PARAM_ERROR);
    }

    private final RemoteMemberService remoteMemberService;

}
