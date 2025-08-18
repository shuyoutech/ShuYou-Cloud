package com.shuyoutech.member.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.member.domain.bo.SocialUserBo;
import com.shuyoutech.member.domain.entity.SocialUserEntity;
import com.shuyoutech.member.domain.vo.SocialUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-22 13:29:35
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialUserServiceImpl extends SuperServiceImpl<SocialUserEntity, SocialUserVo> implements SocialUserService {

    @Override
    public List<SocialUserVo> convertTo(List<SocialUserEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public SocialUserVo convertTo(SocialUserEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(SocialUserBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getSocialType())) {
            query.addCriteria(Criteria.where("socialType").is(bo.getSocialType()));
        }
        if (StringUtils.isNotBlank(bo.getOpenid())) {
            query.addCriteria(Criteria.where("openid").is(bo.getOpenid()));
        }
        return query;
    }

    @Override
    public PageResult<SocialUserVo> page(PageQuery<SocialUserBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SocialUserVo detail(String id) {
        SocialUserEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveSysSocialUser(SocialUserBo bo) {
        SocialUserEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateSysSocialUser(SocialUserBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteSysSocialUser(List<String> ids) {
        return this.deleteByIds(ids);
    }

}