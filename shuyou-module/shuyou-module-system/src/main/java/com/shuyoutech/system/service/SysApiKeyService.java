package com.shuyoutech.system.service;

import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.system.domain.bo.SysApiKeyBo;
import com.shuyoutech.system.domain.entity.SysApiKeyEntity;
import com.shuyoutech.system.domain.vo.SysApiKeyVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-19 16:57:40
 **/
public interface SysApiKeyService extends SuperService<SysApiKeyEntity, SysApiKeyVo> {

    Query buildQuery(SysApiKeyBo bo);

    List<SysApiKeyVo> list(SysApiKeyBo pageQuery);

    SysApiKeyVo detail(String id);

    String saveMemberApiKey(SysApiKeyBo bo);

    boolean updateMemberApiKey(SysApiKeyBo bo);

    boolean deleteMemberApiKey(List<String> ids);

}
