package com.shuyoutech.system.domain.entity;

import com.shuyoutech.api.model.RemoteSysFile;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import com.shuyoutech.system.domain.vo.SysFileVo;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-15 00:09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMappers({@AutoMapper(target = SysFileVo.class), @AutoMapper(target = RemoteSysFile.class), @AutoMapper(target = SysAttachmentEntity.class)})
@Schema(description = "文件表")
@Document(collection = "sys_file")
public class SysFileEntity extends BaseEntity<SysFileEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人ID")
    private String createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

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

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件Hash")
    private String fileHash;

    @Schema(description = "桶名称")
    private String bucketName;

    @Schema(description = "云文件key")
    private String fileKey;

    @Schema(description = "文件url")
    private String fileUrl;

}
