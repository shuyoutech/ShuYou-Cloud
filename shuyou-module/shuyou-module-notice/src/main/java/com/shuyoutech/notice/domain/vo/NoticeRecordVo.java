package com.shuyoutech.notice.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通知记录显示类")
public class NoticeRecordVo extends BaseVo {

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
