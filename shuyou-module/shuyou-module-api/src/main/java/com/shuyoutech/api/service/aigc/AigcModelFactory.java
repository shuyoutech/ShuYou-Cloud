package com.shuyoutech.api.service.aigc;

import com.shuyoutech.api.service.aigc.provider.ModelProvider;
import com.shuyoutech.common.core.exception.BusinessException;
import com.shuyoutech.common.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YangChao
 * @date 2025-07-26 11:28
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AigcModelFactory implements CommandLineRunner, ApplicationContextAware {

    public static final Map<String, ModelProvider> providers = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    /**
     * 获取Model服务
     */
    public static ModelProvider getModelService(String provider) {
        ModelProvider modelProvider = providers.get(provider.toLowerCase());
        if (null == modelProvider) {
            throw new BusinessException(StringUtils.format("provider:{} is not exist", provider));
        }
        return modelProvider;
    }

    @Override
    public void run(String... args) {
        Map<String, ModelProvider> beanMap = applicationContext.getBeansOfType(ModelProvider.class);
        for (ModelProvider modelProvider : beanMap.values()) {
            providers.put(modelProvider.providerName().toLowerCase(), modelProvider);
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
