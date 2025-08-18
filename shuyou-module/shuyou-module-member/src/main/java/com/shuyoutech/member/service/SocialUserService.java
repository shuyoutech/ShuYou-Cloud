package com.shuyoutech.member.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.SocialUserBo;
import com.shuyoutech.member.domain.entity.SocialUserEntity;
import com.shuyoutech.member.domain.vo.SocialUserVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-22 13:29:35
 **/
public interface SocialUserService extends SuperService<SocialUserEntity, SocialUserVo> {

    Query buildQuery(SocialUserBo bo);

    PageResult<SocialUserVo> page(PageQuery<SocialUserBo> pageQuery);

    SocialUserVo detail(String id);

    String saveSysSocialUser(SocialUserBo bo);

    boolean updateSysSocialUser(SocialUserBo bo);

    boolean deleteSysSocialUser(List<String> ids);

}
