package com.shuyoutech.notice.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.notice.domain.vo.NoticeRecordVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NoticeRecordVo.class)
@Document(collection = "notice_record")
@Schema(description = "通知记录表类")
public class NoticeRecordEntity extends BaseEntity<NoticeRecordEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "通知方式")
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
