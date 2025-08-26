package com.shuyoutech.api.service;

import com.shuyoutech.api.domain.bo.ApiInterfaceBo;
import com.shuyoutech.api.domain.entity.ApiInterfaceEntity;
import com.shuyoutech.api.domain.vo.ApiInterfaceVo;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiInterfaceServiceImpl extends SuperServiceImpl<ApiInterfaceEntity, ApiInterfaceVo> implements ApiInterfaceService {

    @Override
    public List<ApiInterfaceVo> convertTo(List<ApiInterfaceEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public ApiInterfaceVo convertTo(ApiInterfaceEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(ApiInterfaceBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<ApiInterfaceVo> page(PageQuery<ApiInterfaceBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public ApiInterfaceVo detail(String id) {
        ApiInterfaceEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveApi(ApiInterfaceBo bo) {
        ApiInterfaceEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateApi(ApiInterfaceBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteApi(List<String> ids) {
        return this.deleteByIds(ids);
    }

}