package com.tlj.aicodegenerator.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.tlj.aicodegenerator.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.tlj.aicodegenerator.model.entity.ChatHistory;
import com.tlj.aicodegenerator.model.entity.User;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href='https://github.com/tuhardy/ai-code-generator'>tlj</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 添加对话历史记录
     * @param appId
     * @param message
     * @param messageType
     * @param userId
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据appId删除对话历史记录
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * 分页查询指定appId的对话历史记录
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 构造查询条件
      * @param request
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request);
}
