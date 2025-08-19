package com.shuyoutech.aigc.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.aigc.domain.bo.ChatModelBo;
import com.shuyoutech.aigc.domain.entity.AigcChatConversationEntity;
import com.shuyoutech.aigc.domain.entity.AigcChatMessageEntity;
import com.shuyoutech.aigc.domain.entity.AigcModelEntity;
import com.shuyoutech.aigc.domain.model.ChatMessage;
import com.shuyoutech.aigc.domain.model.ChatModelBuilder;
import com.shuyoutech.aigc.domain.model.UserModelUsage;
import com.shuyoutech.aigc.provider.AigcModelFactory;
import com.shuyoutech.aigc.provider.service.ModelService;
import com.shuyoutech.aigc.provider.service.impl.OpenRouterService;
import com.shuyoutech.api.service.RemoteAigcService;
import com.shuyoutech.common.core.util.BooleanUtils;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.SpringUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.List;

import static com.shuyoutech.aigc.constant.AiConstants.ROLE_ASSISTANT;
import static com.shuyoutech.aigc.constant.AiConstants.ROLE_USER;

/**
 * @author YangChao
 * @date 2025-08-19 00:00
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteAigcServiceImpl implements RemoteAigcService {

    @Override
    public void chat(String userId, String message, WebSocketSession webSocketSession) {
        try {
            ChatModelBo bo = JSONObject.parseObject(message, ChatModelBo.class);
            String provider = bo.getProvider();
            String modelName = bo.getModel();
            JSONObject modelParam = bo.getModelParam();
            String conversationId = StringUtils.isEmpty(bo.getConversationId()) ? IdUtil.simpleUUID() : bo.getConversationId();
            String userMessage = StringUtils.toStringOrEmpty(modelParam.getString("message"));

            // 开启多轮对话
            boolean enableMemory = modelParam.getBooleanValue("enable_memory", false);
            List<ChatMessage> messages = CollectionUtils.newArrayList();
            if (BooleanUtils.isTrue(enableMemory) && StringUtils.isNotEmpty(bo.getConversationId())) {
                Query query = new Query();
                query.addCriteria(Criteria.where("conversationId").is(bo.getConversationId()));
                Pageable pageable = PageRequest.of(0, 2);
                query.with(pageable);
                query.with(Sort.by(Sort.Direction.DESC, "requestTime"));
                List<AigcChatMessageEntity> chatMessageList = MongoUtils.selectList(query, AigcChatMessageEntity.class);
                if (CollectionUtils.isNotEmpty(chatMessageList)) {
                    for (AigcChatMessageEntity chatMessage : chatMessageList) {
                        messages.add(ChatMessage.builder().role(ROLE_USER).content(chatMessage.getUserMessage()).build());
                        messages.add(ChatMessage.builder().role(ROLE_ASSISTANT).content(chatMessage.getAssistantMessage()).build());
                    }
                }
            }
            AigcModelEntity model = aigcModelService.getModel(provider, modelName);
            if (null == model) {
                log.error("chat =============== provider:{},model:{} is not exist", provider, modelName);
                return;
            }

            AigcChatConversationEntity conversationEntity = MongoUtils.getById(conversationId, AigcChatConversationEntity.class);
            if (null == conversationEntity) {
                AigcChatConversationEntity conversation = new AigcChatConversationEntity();
                conversation.setId(conversationId);
                conversation.setCreateTime(new Date());
                conversation.setTitle(userMessage);
                conversation.setUserId(userId);
                MongoUtils.save(conversation);
            }

            UserModelUsage userModelUsage = new UserModelUsage();
            userModelUsage.setId(IdUtil.fastSimpleUUID());
            userModelUsage.setUserId(userId);
            userModelUsage.setProvider(provider);
            userModelUsage.setModelName(modelName);
            userModelUsage.setConversationId(conversationId);
            userModelUsage.setMessages(messages);
            userModelUsage.setRequestTime(new Date());
            userModelUsage.setModelFunction(bo.getModelFunction());
            userModelUsage.setUserMessage(userMessage);
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest servletRequest = ((ServletRequestAttributes) attributes).getRequest();
                String requestIp = JakartaServletUtil.getClientIP(servletRequest);
                userModelUsage.setIp(requestIp);
            }
            userModelUsage.setModel(model);

            ChatModelBuilder builder = ChatModelBuilder.builder().build();
            builder.setConversationId(conversationId);
            builder.setUserId(userId);
            builder.setBaseUrl(model.getBaseUrl());
            builder.setApiKey(model.getApiKey());
            builder.setProvider(provider);
            builder.setModelName(modelName);
            builder.setModelParam(modelParam);
            builder.setUserToken(userModelUsage);
            if (StringUtils.contains(model.getBaseUrl(), "openrouter")) {
                OpenRouterService modelService = SpringUtils.getBean(OpenRouterService.class);
                modelService.chat(builder, webSocketSession);
            } else {
                ModelService modelService = AigcModelFactory.getModelService(provider);
                modelService.chat(builder, webSocketSession);
            }
        } catch (Exception e) {
            log.error("chat ==================== error:{}", e.getMessage());
        }
    }

    private final AigcModelService aigcModelService;

}
