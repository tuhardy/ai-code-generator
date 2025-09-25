package com.tlj.aicodegenerator.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeepSeekModelConfig {

    /**
     * 复用 yml 里已写的 langchain4j.open-ai 配置项
     */
    @ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
    DeepSeekChatProps props() {
        return new DeepSeekChatProps();
    }

    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(props().getBaseUrl())
                .apiKey(props().getApiKey())
                .modelName(props().getModelName())
                .logRequests(props().isLogRequests())
                .logResponses(props().isLogResponses())
                .strictJsonSchema(props().isStrictJsonSchema())
                .responseFormat(props().getResponseFormat())
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(props().getBaseUrl())
                .apiKey(props().getApiKey())
                .modelName(props().getModelName())
                .logRequests(props().isLogRequests())
                .logResponses(props().isLogResponses())
                .build();
    }

    /* ---------- 简单 POJO 收配置 ---------- */
    static class DeepSeekChatProps {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private boolean logRequests;
        private boolean logResponses;
        private boolean strictJsonSchema;
        private String responseFormat;

        /* 省略 getter/setter，Lombok 可代替 */
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }
        public boolean isLogRequests() { return logRequests; }
        public void setLogRequests(boolean logRequests) { this.logRequests = logRequests; }
        public boolean isLogResponses() { return logResponses; }
        public void setLogResponses(boolean logResponses) { this.logResponses = logResponses; }
        public boolean isStrictJsonSchema() { return strictJsonSchema; }
        public void setStrictJsonSchema(boolean strictJsonSchema) { this.strictJsonSchema = strictJsonSchema; }
        public String getResponseFormat() { return responseFormat; }
        public void setResponseFormat(String responseFormat) { this.responseFormat = responseFormat; }
    }
}