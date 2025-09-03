package com.shuyoutech.system.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.system.domain.bo.SysAttachmentBo;
import com.shuyoutech.system.domain.entity.SysAttachmentEntity;
import com.shuyoutech.system.domain.vo.SysAttachmentVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-09-03 13:17:59
 **/
public interface SysAttachmentService extends SuperService<SysAttachmentEntity, SysAttachmentVo> {

    Query buildQuery(SysAttachmentBo bo);

    PageResult<SysAttachmentVo> page(PageQuery<SysAttachmentBo> pageQuery);

    SysAttachmentVo detail(String id);

    String saveSysAttachment(SysAttachmentBo bo);

    boolean updateSysAttachment(SysAttachmentBo bo);

    boolean deleteSysAttachment(List<String> ids);

    void attachment(String targetType, String targetId, List<String> fileIds);

}
