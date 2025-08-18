package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.notice.domain.bo.SmsTemplateBo;
import com.shuyoutech.notice.domain.entity.SmsTemplateEntity;
import com.shuyoutech.notice.domain.vo.SmsTemplateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsTemplateServiceImpl extends SuperServiceImpl<SmsTemplateEntity, SmsTemplateVo> implements SmsTemplateService {

    @Override
    public List<SmsTemplateVo> convertTo(List<SmsTemplateEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public SmsTemplateVo convertTo(SmsTemplateEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(SmsTemplateBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<SmsTemplateVo> page(PageQuery<SmsTemplateBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SmsTemplateVo detail(String id) {
        SmsTemplateEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveSmsTemplate(SmsTemplateBo bo) {
        SmsTemplateEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateSmsTemplate(SmsTemplateBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteSmsTemplate(List<String> ids) {
        return this.deleteByIds(ids);
    }

}