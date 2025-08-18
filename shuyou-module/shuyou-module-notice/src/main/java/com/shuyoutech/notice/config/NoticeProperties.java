package com.shuyoutech.notice.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-19 14:17
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "shuyoutech.sms")
public class NoticeProperties implements Serializable {

    @Schema(description = "用户key")
    private String accessKey;

    @Schema(description = "用户私钥key")
    private String secretKey;

}