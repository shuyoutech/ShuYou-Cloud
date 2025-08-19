package com.shuyoutech.member.service;

import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.member.domain.bo.MemberApiKeyBo;
import com.shuyoutech.member.domain.entity.MemberApiKeyEntity;
import com.shuyoutech.member.domain.vo.MemberApiKeyVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-19 16:57:40
 **/
public interface MemberApiKeyService extends SuperService<MemberApiKeyEntity, MemberApiKeyVo> {

    Query buildQuery(MemberApiKeyBo bo);

    List<MemberApiKeyVo> list(MemberApiKeyBo pageQuery);

    MemberApiKeyVo detail(String id);

    String saveMemberApiKey(MemberApiKeyBo bo);

    boolean updateMemberApiKey(MemberApiKeyBo bo);

    boolean deleteMemberApiKey(List<String> ids);

}
