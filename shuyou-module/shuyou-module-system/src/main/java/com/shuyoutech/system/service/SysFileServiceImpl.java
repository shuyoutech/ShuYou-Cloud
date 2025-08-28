package com.shuyoutech.system.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.IdUtil;
import com.shuyoutech.common.core.constant.DateConstants;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.common.web.util.JakartaServletUtils;
import com.shuyoutech.system.config.FileStorageProperties;
import com.shuyoutech.system.domain.bo.SysFileBo;
import com.shuyoutech.system.domain.entity.SysFileEntity;
import com.shuyoutech.system.domain.vo.SysFileVo;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2023-07-10 22:41
 **/
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class SysFileServiceImpl extends SuperServiceImpl<SysFileEntity, SysFileVo> implements SysFileService {

    private MinioClient minioClient;
    private String bucketName = null;
    String previewPrefix = null;

    @PostConstruct
    public void init() {
        String accessKey = fileStorageProperties.getAccessKey();
        String secretAccessKey = fileStorageProperties.getSecretKey();
        String endpoint = fileStorageProperties.getEndpoint();
        bucketName = fileStorageProperties.getBucketName();
        previewPrefix = fileStorageProperties.getPreviewPrefix();
        minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretAccessKey).build();
    }

    @Override
    public Query buildQuery(SysFileBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getFileName())) {
            query.addCriteria(Criteria.where("fileName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getFileName()), Pattern.CASE_INSENSITIVE)));
        }
        if (StringUtils.isNotBlank(bo.getOriginalFileName())) {
            query.addCriteria(Criteria.where("originalFileName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getOriginalFileName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public List<SysFileVo> convertTo(List<SysFileEntity> list) {
        List<SysFileVo> result = CollectionUtils.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        list.forEach(e -> {
            SysFileVo vo = MapstructUtils.convert(e, this.voClass);
            vo.setFileSizeFormat(DataSizeUtil.format(e.getFileSize()));
            result.add(vo);
        });
        return result;
    }

    public SysFileVo convertTo(SysFileEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public PageResult<SysFileVo> page(PageQuery<SysFileBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SysFileVo upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileType = FileUtil.extName(originalFilename);
        if (StringUtils.isBlank(fileType)) {
            throw new BusinessException("文件格式有误!");
        }
        long fileSize = file.getSize();
        Date date = new Date();
        String fileName = StringUtils.format("{}.{}", IdUtil.getSnowflakeNextIdStr(), fileType);
        String ossFileKey = StringUtils.format("{}/{}", DateUtils.format(date, DateConstants.PURE_DATE_FORMAT), fileName);
        String filePath = StringUtils.format("{}/{}", fileStorageProperties.getUploadDir(), ossFileKey);
        try {
            SysFileEntity sysFile = new SysFileEntity();
            sysFile.setId(IdUtil.simpleUUID());
            sysFile.setCreateTime(new Date());
            sysFile.setCreateUserId(AuthUtils.getLoginUserId());
            sysFile.setCreateUserName(AuthUtils.getLoginUserName());
            sysFile.setFileName(fileName);
            sysFile.setOriginalFileName(originalFilename);
            sysFile.setOssFileKey(ossFileKey);
            sysFile.setFileSize(fileSize);
            sysFile.setFileType(fileType);
            sysFile.setMimeType(file.getContentType());
            sysFile.setFilePath(filePath);
            sysFile.setBucketName(bucketName);
            FileUtils.writeFromStream(file.getInputStream(), filePath);
            String fileHash = SmUtils.sm3(new File(filePath));
            sysFile.setFileHash(fileHash);

            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder() //
                    .bucket(bucketName) //
                    .object(ossFileKey) //
                    .filename(filePath) //
                    .contentType(file.getContentType()) //
                    .build();
            minioClient.uploadObject(uploadObjectArgs);

            sysFile.setPreviewUrl(StringUtils.format("{}/{}", fileStorageProperties.getPreviewPrefix(), ossFileKey));
            MongoUtils.save(sysFile);
            return MapstructUtils.convert(sysFile, SysFileVo.class);
        } catch (Exception e) {
            log.error("upload =================== exception:{}", e.getMessage());
        } finally {
            FileUtils.del(filePath);
        }
        return null;
    }

    @Override
    public SysFileVo upload(String originalFileName, byte[] data) {
        String fileType = FileUtil.extName(originalFileName);
        if (StringUtils.isBlank(fileType)) {
            throw new BusinessException("文件格式有误!");
        }
        long fileSize = data.length;
        Date date = new Date();
        String fileName = StringUtils.format("{}.{}", IdUtil.getSnowflakeNextIdStr(), fileType);
        String ossFileKey = StringUtils.format("{}/{}", DateUtils.format(date, DateConstants.PURE_DATE_FORMAT), fileName);
        String filePath = StringUtils.format("{}/{}", fileStorageProperties.getUploadDir(), ossFileKey);
        try {
            SysFileEntity sysFile = new SysFileEntity();
            sysFile.setId(IdUtil.simpleUUID());
            sysFile.setCreateTime(new Date());
            sysFile.setCreateUserName(AuthUtils.getLoginUserName());
            sysFile.setCreateUserId(AuthUtils.getLoginUserId());
            sysFile.setFileName(fileName);
            sysFile.setOriginalFileName(originalFileName);
            sysFile.setOssFileKey(ossFileKey);
            sysFile.setFileSize(fileSize);
            sysFile.setFileType(fileType);
            sysFile.setFilePath(filePath);
            sysFile.setBucketName(bucketName);
            FileUtils.writeBytes(data, filePath);
            String fileHash = SmUtils.sm3(new File(filePath));
            sysFile.setFileHash(fileHash);
            sysFile.setMimeType(FileUtils.getMimeType(filePath));

            PutObjectArgs putObjectArgs = PutObjectArgs.builder() //
                    .bucket(bucketName) //
                    .object(ossFileKey) //
                    .stream(IoUtil.toStream(data), fileSize, -1) //
                    .contentType(sysFile.getMimeType()) //
                    .build();
            minioClient.putObject(putObjectArgs);

            sysFile.setPreviewUrl(StringUtils.format("{}/{}", fileStorageProperties.getPreviewPrefix(), ossFileKey));
            MongoUtils.save(sysFile);
            return MapstructUtils.convert(sysFile, SysFileVo.class);
        } catch (Exception e) {
            log.error("upload ==================== exception:{}", e.getMessage());
        } finally {
            FileUtils.del(filePath);
        }
        return null;
    }

    @Override
    public void deleteFileById(String fileId) {
        try {
            SysFileEntity sysFile = MongoUtils.getById(fileId, SysFileEntity.class);
            if (null == sysFile) {
                return;
            }
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(sysFile.getOssFileKey()).build());
        } catch (Exception e) {
            log.error("deleteFileById ==================== exception:{}", e.getMessage());
        } finally {
            MongoUtils.deleteById(fileId, SysFileEntity.class);
        }
    }

    @Override
    public void down(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            SysFileEntity sysFile = getById(id);
            if (null == sysFile) {
                JakartaServletUtils.write(response, 500, "文件数据不存在");
                return;
            }
            String ossFileKey = sysFile.getOssFileKey();
            String originalFileName = sysFile.getOriginalFileName();
            String encodeFileName = FileUtils.encodeFileName(request, originalFileName);
            FileUtils.setAttachmentResponseHeader(response, encodeFileName);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Length", String.valueOf(sysFile.getFileSize()));
            GetObjectResponse ossObject = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(ossFileKey).build());
            InputStream inputStream = IoUtil.toStream(ossObject.readAllBytes());
            response.setHeader("Content-Type", sysFile.getMimeType());
            IoUtil.copy(inputStream, response.getOutputStream());
            IoUtil.close(inputStream);
        } catch (Exception e) {
            log.error("down ===================== exception:{}", e.getMessage());
        }
    }

    @Override
    public String generatedUrl(String ossId) {
        SysFileEntity sysFile = MongoUtils.getById(ossId, SysFileEntity.class);
        if (null == sysFile) {
            throw new BusinessException("该文件ID没有对应记录");
        }
        return sysFile.getPreviewUrl();
    }

    private final FileStorageProperties fileStorageProperties;

}
