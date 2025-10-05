package com.tlj.aicodegenerator.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @author <a href='https://github.com/tuhardy/ai-code-generator'>tlj</a>
 */
@Getter
public enum ChatHistoryMessageTypeEnum {
    USER("用户消息", "user"),
    AI("AI消息", "ai"),
    ERROR("错误消息", "error");

    private final String text;
    private final String value;

    ChatHistoryMessageTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static ChatHistoryMessageTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (ChatHistoryMessageTypeEnum messageTypeEnum : ChatHistoryMessageTypeEnum.values()) {
            if (value.equals(messageTypeEnum.value)) {
                return messageTypeEnum;
            }
        }
        return null;
    }
}
