package com.tlj.aicodegenerator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.tlj.aicodegenerator.constant.UserConstant;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.exception.ThrowUtils;
import com.tlj.aicodegenerator.mapper.ChatHistoryMapper;
import com.tlj.aicodegenerator.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.tlj.aicodegenerator.model.entity.App;
import com.tlj.aicodegenerator.model.entity.ChatHistory;
import com.tlj.aicodegenerator.model.entity.User;
import com.tlj.aicodegenerator.model.enums.ChatHistoryMessageTypeEnum;
import com.tlj.aicodegenerator.service.AppService;
import com.tlj.aicodegenerator.service.ChatHistoryService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层实现。
 *
 * @author <a href='https://github.com/tuhardy/ai-code-generator'>tlj</a>
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{
    @Resource
    @Lazy
    private AppService appService;
    /**
     * 添加对话历史记录。
     * @param appId
     * @param message
     * @param messageType
     * @param userId
     * @return
     */
    @Override
    public boolean addChatMessage(Long appId, String message, String messageType, Long userId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "消息内容不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(messageType), ErrorCode.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        // 验证消息类型是否有效
        ChatHistoryMessageTypeEnum messageTypeEnum = ChatHistoryMessageTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(messageTypeEnum == null, ErrorCode.PARAMS_ERROR, "不支持的消息类型: " + messageType);
        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(message)
                .messageType(messageType)
                .userId(userId)
                .build();
        return this.save(chatHistory);
    }
    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("appId", appId);
        return this.remove(queryWrapper);
    }
    @Override
    public Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                                      LocalDateTime lastCreateTime,
                                                      User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0 || pageSize > 50, ErrorCode.PARAMS_ERROR, "页面大小必须在1-50之间");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 验证权限：只有应用创建者和管理员可以查看
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ErrorCode.NO_AUTH_ERROR, "无权查看该应用的对话历史");
        // 构建查询条件
        ChatHistoryQueryRequest queryRequest = new ChatHistoryQueryRequest();
        queryRequest.setAppId(appId);
        queryRequest.setLastCreateTime(lastCreateTime);
        QueryWrapper queryWrapper = this.getQueryWrapper(queryRequest);
        // 查询数据
        return this.page(Page.of(1, pageSize), queryWrapper);
    }

    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if(request==null){
            return queryWrapper;
        }
        Long id = request.getId();
        String message = request.getMessage();
        String messageType = request.getMessageType();
        Long appId = request.getAppId();
        Long userId = request.getUserId();
        LocalDateTime lastCreateTime = request.getLastCreateTime();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        queryWrapper.eq("id", id)
                .eq("appId", appId)
                .eq("messageType", messageType)
                .eq("userId", userId)
                .like("message", message);
        //根据会话历史记录创建时间进行游标查询
        if(lastCreateTime!=null){
            queryWrapper.le("create_time", lastCreateTime);
        }
        //排序
        if(StrUtil.isNotBlank(sortField)){
            queryWrapper.orderBy(sortField,"ascend".equals(sortOrder));
        }else {
            queryWrapper.orderBy("create_time",false);
        }
        return queryWrapper;
    }
}
