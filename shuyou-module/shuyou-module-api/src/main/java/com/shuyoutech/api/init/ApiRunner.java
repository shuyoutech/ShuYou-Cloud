package com.shuyoutech.api.init;

import cn.hutool.core.util.IdUtil;
import com.shuyoutech.api.domain.entity.ApiInterfaceEntity;
import com.shuyoutech.api.service.api.ApiService;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.StreamUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author YangChao
 * @date 2025-08-10 15:13
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRunner implements CommandLineRunner, ApplicationContextAware {

    public static final Map<String, ApiService> serviceMap = new ConcurrentHashMap<>();
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get(APPLICATION_JSON_VALUE);
    private ApplicationContext applicationContext;

    public static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder() //
            .connectTimeout(3, TimeUnit.MINUTES) // 3分
            .readTimeout(5, TimeUnit.MINUTES) // 5分
            .writeTimeout(5, TimeUnit.MINUTES) // 5分
            .build();

    @Override
    public void run(String... args) {
        initApiService();
        initApiInterface();
    }

    private void initApiService() {
        Map<String, ApiService> beanMap = applicationContext.getBeansOfType(ApiService.class);
        for (ApiService apiService : beanMap.values()) {
            serviceMap.put(apiService.interfaceName().toLowerCase(), apiService);
        }
    }

    private void initApiInterface() {
        List<ApiInterfaceEntity> interfaceList = MongoUtils.selectList(ApiInterfaceEntity.class);
        List<String> codeList = StreamUtils.toList(interfaceList, ApiInterfaceEntity::getInterfaceCode);
        if (!CollectionUtils.contains(codeList, "aigc.chatCompletions")) {
            ApiInterfaceEntity entity = new ApiInterfaceEntity();
            entity.setId(IdUtil.simpleUUID());
            entity.setInterfaceCode("aigc.chatCompletions");
            entity.setInterfaceName("对话补全接口");
            entity.setInterfaceClass("");
            entity.setInterfaceDesc("AI对话聊天接口");
            MongoUtils.save(entity);
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
