package com.shuyoutech.member.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.SocialClientToken;
import com.shuyoutech.api.model.SocialUserInfo;
import com.shuyoutech.common.core.enums.StatusEnum;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.SmUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import com.shuyoutech.member.domain.bo.MemberUserBo;
import com.shuyoutech.member.domain.bo.SmsLoginBo;
import com.shuyoutech.member.domain.entity.MemberUserBind;
import com.shuyoutech.member.domain.entity.MemberUserEntity;
import com.shuyoutech.member.domain.entity.MemberWalletEntity;
import com.shuyoutech.member.domain.entity.SocialUserEntity;
import com.shuyoutech.member.domain.vo.MemberUserVo;
import com.shuyoutech.member.socail.SocialClientRequest;
import com.shuyoutech.member.socail.SocialClientRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.shuyoutech.common.redis.constant.CacheConstants.CAPTCHA_SMS_KEY;

/**
 * @author YangChao
 * @date 2025-08-01 15:16:55
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUserServiceImpl extends SuperServiceImpl<MemberUserEntity, MemberUserVo> implements MemberUserService {

    @Override
    public List<MemberUserVo> convertTo(List<MemberUserEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public MemberUserVo convertTo(MemberUserEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(MemberUserBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        MemberUserEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<MemberUserVo> page(PageQuery<MemberUserBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public MemberUserVo detail(String id) {
        MemberUserEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveClientUser(MemberUserBo bo) {
        MemberUserEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateClientUser(MemberUserBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteClientUser(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public MemberUserEntity register(String mobile) {
        Query query = new Query();
        query.addCriteria(Criteria.where("mobile").is(mobile));
        MemberUserEntity memberUser = this.selectOne(query);
        if (null == memberUser) {
            String userId = IdUtil.simpleUUID();
            memberUser = new MemberUserEntity();
            memberUser.setId(userId);
            memberUser.setCreateTime(new Date());
            memberUser.setStatus(StatusEnum.ENABLE.getValue());
            memberUser.setNickname(mobile);
            memberUser.setMobile(mobile);
            memberUser.setLoginDate(new Date());
            memberUser.setLoginIp(JakartaServletUtils.getClientIP(JakartaServletUtils.getRequest()));

            MemberWalletEntity wallet = new MemberWalletEntity();
            wallet.setId(userId);
            wallet.setBalance(BigDecimal.valueOf(1));
            wallet.setTotalExpense(BigDecimal.ZERO);
            wallet.setTotalRecharge(BigDecimal.ZERO);
            MongoUtils.save(wallet);
            return MongoUtils.save(memberUser);
        }
        return memberUser;
    }

    @Override
    public MemberUserEntity register(SocialUserInfo userInfo) {
        String socialType = userInfo.getSocialType();
        SocialClientToken token = userInfo.getToken();
        String openId = token.getOpenId();

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("binds.socialType").is(socialType));
        query1.addCriteria(Criteria.where("binds.socialUserId").is(openId));
        MemberUserEntity memberUser = MongoUtils.selectOne(query1, MemberUserEntity.class);
        if (null == memberUser) {
            SocialUserEntity socialUser = new SocialUserEntity();
            socialUser.setId(SmUtils.sm3(socialType + openId));
            socialUser.setCreateTime(new Date());
            socialUser.setSocialType(socialType);
            socialUser.setOpenid(openId);
            socialUser.setAccessToken(token.getAccessToken());
            socialUser.setNickname(socialUser.getNickname());
            socialUser.setAvatar(socialUser.getAvatar());
            MongoUtils.save(socialUser);

            memberUser = new MemberUserEntity();
            memberUser.setId(IdUtil.simpleUUID());
            memberUser.setStatus(StatusEnum.DISABLE.getValue());
            memberUser.setCreateTime(new Date());
            memberUser.setNickname(socialUser.getNickname());
            memberUser.setAvatar(socialUser.getAvatar());
            MemberUserBind bind = new MemberUserBind();
            bind.setSocialType(socialType);
            bind.setSocialUserId(openId);
            memberUser.setBinds(List.of(bind));
            memberUser.setLoginDate(new Date());
            memberUser.setLoginIp(JakartaServletUtils.getClientIP(JakartaServletUtils.getRequest()));

            MemberWalletEntity wallet = new MemberWalletEntity();
            wallet.setId(memberUser.getId());
            wallet.setBalance(BigDecimal.valueOf(1));
            wallet.setTotalExpense(BigDecimal.ZERO);
            wallet.setTotalRecharge(BigDecimal.ZERO);
            MongoUtils.save(wallet);

            return MongoUtils.save(memberUser);
        } else {
            Update update = new Update();
            update.set("loginDate", new Date());
            update.set("loginIp", JakartaServletUtils.getClientIP(JakartaServletUtils.getRequest()));
            MongoUtils.patch(memberUser.getId(), update, MemberUserEntity.class);
        }
        return memberUser;
    }

    @Override
    public MemberUserEntity getMemberProfile() {
        String userId = AuthUtils.getLoginUserId();
        MemberUserEntity user = this.getById(userId);
        if (null == user) {
            log.error("getMemberProfile ========== userId:{} is not exist", userId);
            throw new BusinessException("获取用户信息失败");
        }
        return user;
    }

    @Override
    public void socialUserBind(AuthAccessToken bo) {
        String socialType = bo.getSocialType();
        SocialClientRequest socialClientRequest = socialClientRequestFactory.getAuthRequest(socialType);
        SocialUserInfo userInfo = socialClientRequest.login(bo);
        log.info("socialUserBind ======================== socialUser:{}", JSONObject.toJSONString(userInfo));
        String userId = AuthUtils.getLoginUserId();
        MemberUserEntity user = this.getById(userId);
        if (null == user) {
            log.error("socialUserBind ========== userId:{} is not exist", userId);
            throw new BusinessException("获取用户信息失败");
        }
        SocialClientToken socialToken = userInfo.getToken();
        String openId = socialToken.getOpenId();

        SocialUserEntity socialUser = new SocialUserEntity();
        socialUser.setId(SmUtils.sm3(socialType + openId));
        socialUser.setCreateTime(new Date());
        socialUser.setSocialType(socialType);
        socialUser.setOpenid(openId);
        socialUser.setAccessToken(socialToken.getAccessToken());
        socialUser.setNickname(userInfo.getNickname());
        socialUser.setAvatar(userInfo.getAvatar());
        MongoUtils.save(socialUser);

        MemberUserBind bind = new MemberUserBind();
        bind.setSocialType(socialType);
        bind.setSocialUserId(openId);
        List<MemberUserBind> binds = user.getBinds();
        if (CollectionUtils.isEmpty(binds)) {
            binds = List.of(bind);
        } else {
            binds.add(bind);
        }
        Update update = new Update();
        update.set("binds", binds);
        update.set("status", StatusEnum.ENABLE.getValue());
        MongoUtils.patch(userId, update, MemberUserEntity.class);
    }

    @Override
    public void mobileBind(SmsLoginBo bo) {
        String captcha = RedisUtils.getString(CAPTCHA_SMS_KEY + bo.getMobile());
        if (!bo.getCode().equals(captcha)) {
            throw new BusinessException("手机验证码校验失败!");
        }
        String userId = AuthUtils.getLoginUserId();
        MemberUserEntity user = this.getById(userId);
        if (null == user) {
            log.error("mobileBind ========== userId:{} is not exist!", userId);
            throw new BusinessException("获取用户信息失败");
        }
        Update update = new Update();
        update.set("mobile", bo.getMobile());
        MongoUtils.patch(userId, update, MemberUserEntity.class);
        RedisUtils.delete(CAPTCHA_SMS_KEY + bo.getMobile());
    }

    private final SocialClientRequestFactory socialClientRequestFactory;
}