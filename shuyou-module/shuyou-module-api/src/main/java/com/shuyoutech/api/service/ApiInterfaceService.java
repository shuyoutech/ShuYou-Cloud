package com.shuyoutech.api.service;

import com.shuyoutech.api.domain.bo.ApiInterfaceBo;
import com.shuyoutech.api.domain.entity.ApiInterfaceEntity;
import com.shuyoutech.api.domain.vo.ApiInterfaceVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-22 14:26:07
 **/
public interface ApiInterfaceService extends SuperService<ApiInterfaceEntity, ApiInterfaceVo> {

    Query buildQuery(ApiInterfaceBo bo);

    PageResult<ApiInterfaceVo> page(PageQuery<ApiInterfaceBo> pageQuery);

    ApiInterfaceVo detail(String id);

    String saveApi(ApiInterfaceBo bo);

    boolean updateApi(ApiInterfaceBo bo);

    boolean deleteApi(List<String> ids);
}
