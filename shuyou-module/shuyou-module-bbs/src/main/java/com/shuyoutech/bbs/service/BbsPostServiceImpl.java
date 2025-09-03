package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.BbsPostBo;
import com.shuyoutech.bbs.domain.entity.BbsPostEntity;
import com.shuyoutech.bbs.domain.vo.BbsPostVo;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.RegionUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-09-03 13:41:18
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class BbsPostServiceImpl extends SuperServiceImpl<BbsPostEntity, BbsPostVo> implements BbsPostService {

    @Override
    public List<BbsPostVo> convertTo(List<BbsPostEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public BbsPostVo convertTo(BbsPostEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(BbsPostBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        BbsPostEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<BbsPostVo> page(PageQuery<BbsPostBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public BbsPostVo detail(String id) {
        BbsPostEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveBbsPost(BbsPostBo bo) {
        BbsPostEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateBbsPost(BbsPostBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteBbsPost(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public boolean statusBbsPost(String id, String status) {
        BbsPostEntity entity = this.getById(id);
        if (null == entity) {
            return false;
        }
        Update update = new Update();
        update.set("status", status);
        return MongoUtils.patch(id, update, BbsPostEntity.class);
    }

    @Override
    public String publishPost(BbsPostBo bo) {
        String ip = JakartaServletUtils.getClientIP(JakartaServletUtils.getRequest());
        BbsPostEntity entity = MapstructUtils.convert(bo, getEntityClass());
        entity.setStatus("3");
        entity.setCreateTime(new Date());
        entity.setUserId(AuthUtils.getLoginUserId());
        entity.setIpAddress(RegionUtils.getCity(ip));
        BbsPostEntity post = MongoUtils.save(entity);
        return null == post ? null : post.getId();
    }

}