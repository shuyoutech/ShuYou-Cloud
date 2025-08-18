package com.shuyoutech.member.service;

import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.member.domain.bo.SocialClientBo;
import com.shuyoutech.member.domain.entity.SocialClientEntity;
import com.shuyoutech.member.domain.vo.SocialClientVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-21 16:59:07
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialClientServiceImpl extends SuperServiceImpl<SocialClientEntity, SocialClientVo> implements SocialClientService {

    @Override
    public List<SocialClientVo> convertTo(List<SocialClientEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public SocialClientVo convertTo(SocialClientEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(SocialClientBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getClientName())) {
            query.addCriteria(Criteria.where("clientName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getClientName()), Pattern.CASE_INSENSITIVE)));
        }
        if (null != bo.getSocialType()) {
            query.addCriteria(Criteria.where("socialType").is(bo.getSocialType()));
        }
        if (StringUtils.isNotBlank(bo.getClientId())) {
            query.addCriteria(Criteria.where("clientId").regex(Pattern.compile(String.format("^.*%s.*$", bo.getClientId()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public PageResult<SocialClientVo> page(PageQuery<SocialClientBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SocialClientVo detail(String id) {
        SocialClientEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveSysSocialClient(SocialClientBo bo) {
        SocialClientEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateSysSocialClient(SocialClientBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteSysSocialClient(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public boolean statusSysSocialClient(String id, String status) {
        SocialClientEntity entity = this.getById(id);
        if (null == entity) {
            return false;
        }
        Update update = new Update();
        update.set("status", status);
        return MongoUtils.patch(id, update, SocialClientEntity.class);
    }

    @Override
    public SocialClientEntity socialClientBySocialType(String socialType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("socialType").is(socialType));
        return this.selectOne(query);
    }

}