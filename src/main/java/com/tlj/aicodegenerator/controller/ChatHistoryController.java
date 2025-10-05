package com.tlj.aicodegenerator.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.tlj.aicodegenerator.annotation.AuthCheck;
import com.tlj.aicodegenerator.common.BaseResponse;
import com.tlj.aicodegenerator.common.ResultUtils;
import com.tlj.aicodegenerator.constant.UserConstant;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.exception.ThrowUtils;
import com.tlj.aicodegenerator.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.tlj.aicodegenerator.model.entity.ChatHistory;
import com.tlj.aicodegenerator.model.entity.User;
import com.tlj.aicodegenerator.service.ChatHistoryService;
import com.tlj.aicodegenerator.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对话历史 控制层。
 *
 * @author <a href='https://github.com/tuhardy/ai-code-generator'>tlj</a>
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private UserService userService;


    /**
     * 分页查询某个应用的对话历史（游标查询）
     *
     * @param appId          应用ID
     * @param pageSize       页面大小
     * @param lastCreateTime 最后一条记录的创建时间
     * @param request        请求
     * @return 对话历史分页
     */
    @GetMapping("/app/{appId}")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) LocalDateTime lastCreateTime,
                                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }
    /**
     * 管理员分页查询所有对话历史
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 对话历史分页
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }


}
