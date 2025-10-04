package com.tlj.aicodegenerator.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.tlj.aicodegenerator.model.entity.ChatHistory;
import com.tlj.aicodegenerator.mapper.ChatHistoryMapper;
import com.tlj.aicodegenerator.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author <a href='https://github.com/tlj-x'>tlj</a>
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
