package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.notice.domain.bo.EmailTemplateBo;
import com.shuyoutech.notice.domain.entity.EmailTemplateEntity;
import com.shuyoutech.notice.domain.vo.EmailTemplateVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 21:55:39
 **/
public interface EmailTemplateService extends SuperService<EmailTemplateEntity, EmailTemplateVo> {

    Query buildQuery(EmailTemplateBo bo);

    PageResult<EmailTemplateVo> page(PageQuery<EmailTemplateBo> pageQuery);

    EmailTemplateVo detail(String id);

    String saveEmailTemplate(EmailTemplateBo bo);

    boolean updateEmailTemplate(EmailTemplateBo bo);

    boolean deleteEmailTemplate(List<String> ids);

}
