package com.shuyoutech.system.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.system.domain.vo.SysAttachmentVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-09-03 13:17:59
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysAttachmentVo.class)
@Document(collection = "sys_attachment")
@Schema(description = "附件表类")
public class SysAttachmentEntity extends BaseEntity<SysAttachmentEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "业务类型: post-帖子")
    private String targetType;

    @Schema(description = "业务ID")
    private String targetId;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "原文件名称")
    private String originalFileName;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件格式")
    private String fileType;

    @Schema(description = "媒体类型")
    private String mimeType;

    @Schema(description = "文件Hash")
    private String fileHash;

    @Schema(description = "桶名称")
    private String bucketName;

    @Schema(description = "云文件key")
    private String fileKey;

    @Schema(description = "文件url")
    private String fileUrl;

}
