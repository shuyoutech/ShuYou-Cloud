package com.shuyoutech.system.service;

import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.system.config.OssProperties;
import com.shuyoutech.system.domain.bo.SysAttachmentBo;
import com.shuyoutech.system.domain.entity.SysAttachmentEntity;
import com.shuyoutech.system.domain.entity.SysFileEntity;
import com.shuyoutech.system.domain.vo.SysAttachmentVo;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.shuyoutech.system.service.SysFileServiceImpl.minioClient;

/**
 * @author YangChao
 * @date 2025-09-03 13:17:59
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAttachmentServiceImpl extends SuperServiceImpl<SysAttachmentEntity, SysAttachmentVo> implements SysAttachmentService {

    private OSS ossClient;
    private String bucketName = null;

    @PostConstruct
    public void init() {
        String accessKey = ossProperties.getAccessKey();
        String secretAccessKey = ossProperties.getSecretKey();
        String endpoint = ossProperties.getEndpoint();
        bucketName = ossProperties.getBucketName();
        ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretAccessKey);
    }

    @Override
    public List<SysAttachmentVo> convertTo(List<SysAttachmentEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public SysAttachmentVo convertTo(SysAttachmentEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(SysAttachmentBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<SysAttachmentVo> page(PageQuery<SysAttachmentBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SysAttachmentVo detail(String id) {
        SysAttachmentEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveSysAttachment(SysAttachmentBo bo) {
        SysAttachmentEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateSysAttachment(SysAttachmentBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteSysAttachment(List<String> ids) {
        return this.deleteByIds(ids);
    }

    private void attachment(String targetType, String targetId, String fileId) {
        SysFileEntity file = sysFileService.getById(fileId);
        if (null == file) {
            return;
        }
        String fileKey = file.getFileKey();
        try {
            GetObjectResponse ossObject = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileKey).build());
            InputStream inputStream = IoUtil.toStream(ossObject.readAllBytes());
            PutObjectRequest request = new PutObjectRequest(bucketName, fileKey, inputStream);
            ossClient.putObject(request);
        } catch (Exception e) {
            log.error("attachment =================== exception:{}", e.getMessage());
        }
        SysAttachmentEntity attachment = MapstructUtils.convert(file, SysAttachmentEntity.class);
        attachment.setTargetType(targetType);
        attachment.setTargetId(targetId);
        MongoUtils.save(attachment);
    }

    @Async
    @Override
    public void attachment(String targetType, String targetId, List<String> fileIds) {
        deleteSysAttachment(fileIds);
        for (String fileId : fileIds) {
            attachment(targetType, targetId, fileId);
        }
    }

    private final OssProperties ossProperties;
    private final SysFileService sysFileService;

}