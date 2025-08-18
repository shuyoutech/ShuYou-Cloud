package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.notice.domain.bo.EmailTemplateBo;
import com.shuyoutech.notice.domain.entity.EmailTemplateEntity;
import com.shuyoutech.notice.domain.vo.EmailTemplateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl extends SuperServiceImpl<EmailTemplateEntity, EmailTemplateVo> implements EmailTemplateService {

    @Override
    public List<EmailTemplateVo> convertTo(List<EmailTemplateEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public EmailTemplateVo convertTo(EmailTemplateEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(EmailTemplateBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<EmailTemplateVo> page(PageQuery<EmailTemplateBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public EmailTemplateVo detail(String id) {
        EmailTemplateEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveEmailTemplate(EmailTemplateBo bo) {
        EmailTemplateEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateEmailTemplate(EmailTemplateBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteEmailTemplate(List<String> ids) {
        return this.deleteByIds(ids);
    }

}