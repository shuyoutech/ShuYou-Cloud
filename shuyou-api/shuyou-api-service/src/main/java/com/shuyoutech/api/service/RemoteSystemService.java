package com.shuyoutech.api.service;

import com.shuyoutech.api.model.RemoteSysFile;
import com.shuyoutech.common.satoken.model.LoginUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author YangChao
 * @date 2025-07-07 15:49
 **/
public interface RemoteSystemService {

    LoginUser getUserByUsername(String username);

    Boolean passwordMatch(String rawPassword, String encodedPassword);

    Map<String, String> getUserName(Set<String> userIds);

    Map<String, String> translateByDictCode(String dictCode);

    Set<String> getMenuPermission(String userId);

    Set<String> getRolePermission(String userId);

    RemoteSysFile getFileById(String fileId);

    String getFilePath(String fileId);

    String generatedUrl(String ossId);

    RemoteSysFile upload(String originalFilename, byte[] data);

    RemoteSysFile upload(MultipartFile file);

    void attachment(String targetType, String targetId, String fileId);

    void attachment(String targetType, String targetId, List<String> fileIds);
}
