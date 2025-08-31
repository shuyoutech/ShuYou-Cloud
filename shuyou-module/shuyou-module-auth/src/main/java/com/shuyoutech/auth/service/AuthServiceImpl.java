package com.shuyoutech.auth.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.shuyoutech.api.enums.UserTypeEnum;
import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.RemoteMemberUser;
import com.shuyoutech.api.model.SocialUserInfo;
import com.shuyoutech.api.service.RemoteMemberService;
import com.shuyoutech.api.service.RemoteNoticeService;
import com.shuyoutech.api.service.RemoteSystemService;
import com.shuyoutech.auth.domain.bo.AuthAuthorizeBo;
import com.shuyoutech.auth.domain.bo.AuthLoginBo;
import com.shuyoutech.auth.domain.bo.SmsLoginBo;
import com.shuyoutech.auth.domain.bo.SmsSendBo;
import com.shuyoutech.auth.domain.vo.AuthLoginVo;
import com.shuyoutech.common.core.constant.CommonConstants;
import com.shuyoutech.common.core.enums.ErrorCodeEnum;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.satoken.constant.AuthConstants;
import com.shuyoutech.common.satoken.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YangChao
 * @date 2025-07-19 11:14
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginUser authenticate(String username, String password) {
        LoginUser user = remoteSystemService.getUserByUsername(username);
        if (null == user) {
            throw new BusinessException(ErrorCodeEnum.BAD_CREDENTIALS);
        }
        if (!remoteSystemService.passwordMatch(password, user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.BAD_CREDENTIALS);
        }
        if (StatusEnum.DISABLE.getValue().equals(user.getStatus())) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_DISABLED);
        }
        return user;
    }

    @Override
    public AuthLoginVo login(AuthLoginBo bo) {
        String username = bo.getUsername();
        String password = bo.getPassword();
        LoginUser user = authenticate(username, password);
        SaLoginParameter parameter = new SaLoginParameter();
        parameter.setExtra(CommonConstants.USER_ID, user.getId());
        if (StringUtils.isNotBlank(user.getRealName())) {
            parameter.setExtra(CommonConstants.USER_NAME, user.getRealName());
        } else if (StringUtils.isNotBlank(user.getNickname())) {
            parameter.setExtra(CommonConstants.USER_NAME, user.getNickname());
        } else {
            parameter.setExtra(CommonConstants.USER_NAME, username);
        }
        parameter.setExtra(CommonConstants.USER_TYPE, UserTypeEnum.ADMIN.getValue());
        StpUtil.login(user.getId(), parameter);
        StpUtil.getTokenSession().set(AuthConstants.LOGIN_USER, user);
        return AuthLoginVo.builder().userId(user.getId()).accessToken(StpUtil.getTokenValue()).expireTime(StpUtil.getTokenTimeout()).build();
    }

    @Override
    public AuthLoginVo smsLogin(SmsLoginBo bo) {
/*        String captcha = RedisUtils.getString(CAPTCHA_SMS_KEY + bo.getMobile());
        if (!bo.getCode().equals(captcha)) {
            throw new BusinessException("手机验证码校验失败");
        }*/
        RemoteMemberUser memberUser = remoteMemberService.register(bo.getMobile());
        SaLoginParameter parameter = new SaLoginParameter();
        parameter.setExtra(CommonConstants.USER_ID, memberUser.getId());
        parameter.setExtra(CommonConstants.USER_NAME, memberUser.getNickname());
        parameter.setExtra(CommonConstants.USER_TYPE, UserTypeEnum.MEMBER.getValue());
        StpUtil.login(memberUser.getId(), parameter);
        StpUtil.getTokenSession().set(AuthConstants.LOGIN_USER, memberUser);
       // RedisUtils.delete(CAPTCHA_SMS_KEY + bo.getMobile());
        return AuthLoginVo.builder() //
                .userId(memberUser.getId()) //
                .accessToken(StpUtil.getTokenValue()) //
                .expireTime(StpUtil.getTokenTimeout()) //
                .build();
    }

    @Override
    public void sendSms(SmsSendBo bo) {
        remoteNoticeService.sendSms(bo.getTemplateCode(), bo.getMobile());
    }

    @Override
    public String authorize(AuthAuthorizeBo bo) {
        return remoteMemberService.authorize(bo.getSocialType(), bo.getCallBackSuffix());
    }

    @Override
    public AuthLoginVo accessToken(AuthAccessToken bo) {
        SocialUserInfo socialUser = remoteMemberService.login(bo);
        RemoteMemberUser memberUser = remoteMemberService.register(socialUser);
        SaLoginParameter parameter = new SaLoginParameter();
        parameter.setExtra(CommonConstants.USER_ID, memberUser.getId());
        parameter.setExtra(CommonConstants.USER_NAME, memberUser.getNickname());
        parameter.setExtra(CommonConstants.USER_TYPE, UserTypeEnum.MEMBER.getValue());
        parameter.setExtra(CommonConstants.USER_OPENID, socialUser.getToken().getOpenId());
        StpUtil.login(memberUser.getId(), parameter);
        StpUtil.getTokenSession().set(AuthConstants.LOGIN_USER, memberUser);
        return AuthLoginVo.builder() //
                .userId(memberUser.getId()) //
                .accessToken(StpUtil.getTokenValue()) //
                .expireTime(StpUtil.getTokenTimeout()) //
                .build();
    }

    @Override
    public void logout() {
        try {
            SaSession session = StpUtil.getTokenSession();
            if (ObjectUtil.isNull(session)) {
                return;
            }
            StpUtil.logout();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    private final RemoteSystemService remoteSystemService;
    private final RemoteMemberService remoteMemberService;
    private final RemoteNoticeService remoteNoticeService;

}
