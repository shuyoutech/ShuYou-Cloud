package com.shuyoutech.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YangChao
 * @date 2025-05-15 13:57
 **/
@Data
@Schema(description = "文件附件类")
public class RemoteSysAttachment implements Serializable {

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
