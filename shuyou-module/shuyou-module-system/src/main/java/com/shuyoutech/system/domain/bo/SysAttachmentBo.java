package com.shuyoutech.system.domain.bo;

import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import com.shuyoutech.system.domain.entity.SysAttachmentEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-09-03 13:17:59
 **/
@Data
@AutoMapper(target = SysAttachmentEntity.class)
@Schema(description = "附件类")
public class SysAttachmentBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

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
