package com.shuyoutech.notice.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.notice.domain.entity.NoticeRecordEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
@Data
@AutoMapper(target = NoticeRecordEntity.class)
@Schema(description = "通知记录类")
public class NoticeRecordBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "通知方式(sms,email)")
    private String type;

    @Schema(description = "收件人")
    private String recipient;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知正文")
    private String content;
}
