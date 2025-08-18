package com.shuyoutech.member.service;

import com.shuyoutech.api.model.AuthAccessToken;
import com.shuyoutech.api.model.SocialUserInfo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.MemberUserBo;
import com.shuyoutech.member.domain.bo.SmsLoginBo;
import com.shuyoutech.member.domain.entity.MemberUserEntity;
import com.shuyoutech.member.domain.vo.MemberUserVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-01 15:16:55
 **/
public interface MemberUserService extends SuperService<MemberUserEntity, MemberUserVo> {

    Query buildQuery(MemberUserBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<MemberUserVo> page(PageQuery<MemberUserBo> pageQuery);

    MemberUserVo detail(String id);

    String saveClientUser(MemberUserBo bo);

    boolean updateClientUser(MemberUserBo bo);

    boolean deleteClientUser(List<String> ids);

    MemberUserEntity register(String mobile);

    MemberUserEntity register(SocialUserInfo socialUser);

    /**
     * 获取用户信息
     */
    MemberUserEntity getMemberProfile();

    /**
     * 社交绑定
     */
    void socialUserBind(AuthAccessToken bo);

    /**
     * 手机号绑定
     */
    void mobileBind(SmsLoginBo bo);

}
