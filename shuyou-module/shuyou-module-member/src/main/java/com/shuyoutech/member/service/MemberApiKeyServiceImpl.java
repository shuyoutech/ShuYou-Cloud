package com.shuyoutech.member.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.member.domain.bo.MemberApiKeyBo;
import com.shuyoutech.member.domain.entity.MemberApiKeyEntity;
import com.shuyoutech.member.domain.vo.MemberApiKeyVo;
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
 * @date 2025-08-19 16:57:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberApiKeyServiceImpl extends SuperServiceImpl<MemberApiKeyEntity, MemberApiKeyVo> implements MemberApiKeyService {

    @Override
    public List<MemberApiKeyVo> convertTo(List<MemberApiKeyEntity> list) {
        List<MemberApiKeyVo> result = CollectionUtils.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        list.forEach(e -> {
            MemberApiKeyVo vo = MapstructUtils.convert(e, this.voClass);
            vo.setApiKey(StrUtil.hide(e.getApiKey(), 8, 31));
            result.add(vo);
        });
        return result;
    }

    public MemberApiKeyVo convertTo(MemberApiKeyEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(MemberApiKeyBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getUserId())) {
            query.addCriteria(Criteria.where("userId").is(bo.getUserId()));
        }
        if (StringUtils.isNotBlank(bo.getApiName())) {
            query.addCriteria(Criteria.where("apiName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getApiName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public List<MemberApiKeyVo> list(MemberApiKeyBo bo) {
        bo.setUserId(AuthUtils.getLoginUserId());
        Query query = buildQuery(bo);
        return this.selectListVo(query);
    }

    @Override
    public MemberApiKeyVo detail(String id) {
        MemberApiKeyEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveMemberApiKey(MemberApiKeyBo bo) {
        bo.setUserId(AuthUtils.getLoginUserId());
        bo.setApiKey("sk-" + IdUtil.simpleUUID());
        MemberApiKeyEntity entity = this.save(bo);
        return null == entity ? null : entity.getApiKey();
    }

    @Override
    public boolean updateMemberApiKey(MemberApiKeyBo bo) {
        Update update = new Update();
        update.set("apiName", bo.getApiName());
        return MongoUtils.patch(bo.getId(), update, MemberApiKeyEntity.class);
    }

    @Override
    public boolean deleteMemberApiKey(List<String> ids) {
        return this.deleteByIds(ids);
    }

}