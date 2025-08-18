package com.shuyoutech.member.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.SocialClientBo;
import com.shuyoutech.member.domain.entity.SocialClientEntity;
import com.shuyoutech.member.domain.vo.SocialClientVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-21 16:59:07
 **/
public interface SocialClientService extends SuperService<SocialClientEntity, SocialClientVo> {

    Query buildQuery(SocialClientBo bo);

    PageResult<SocialClientVo> page(PageQuery<SocialClientBo> pageQuery);

    SocialClientVo detail(String id);

    String saveSysSocialClient(SocialClientBo bo);

    boolean updateSysSocialClient(SocialClientBo bo);

    boolean deleteSysSocialClient(List<String> ids);

    boolean statusSysSocialClient(String id, String status);

    SocialClientEntity socialClientBySocialType(String socialType);

}
