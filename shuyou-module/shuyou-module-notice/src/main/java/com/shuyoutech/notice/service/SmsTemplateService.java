package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.notice.domain.bo.SmsTemplateBo;
import com.shuyoutech.notice.domain.entity.SmsTemplateEntity;
import com.shuyoutech.notice.domain.vo.SmsTemplateVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-31 22:37:56
 **/
public interface SmsTemplateService extends SuperService<SmsTemplateEntity, SmsTemplateVo> {

    Query buildQuery(SmsTemplateBo bo);

    PageResult<SmsTemplateVo> page(PageQuery<SmsTemplateBo> pageQuery);

    SmsTemplateVo detail(String id);

    String saveSmsTemplate(SmsTemplateBo bo);

    boolean updateSmsTemplate(SmsTemplateBo bo);

    boolean deleteSmsTemplate(List<String> ids);

}
