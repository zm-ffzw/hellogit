package com.sky.config;

import com.sky.chains.MysqlChatMemory;
import com.sky.chains.OrderQueryTool;
import com.sky.mapper.ChatMemoryMapper;
import com.sky.service.AiChatService;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//AI创建工厂类
@Configuration
public class AiChatFactory {

    @Autowired
    private ChatModel qwenChatModel;
    @Autowired
    private StreamingChatModel qwenStreamingChatModel;
    @Autowired
    OrderQueryTool orderQueryTool;
    @Autowired
    private ContentRetriever contentRetriever;
    @Autowired
    private ChatMemoryMapper chatMemoryMapper;

    @Bean
    public AiChatService aiChatService() {
        //创建会话提供者
        ChatMemoryProvider chatMemoryProvider = memoryId ->
                new MysqlChatMemory(String.valueOf(memoryId), chatMemoryMapper);
        return AiServices.builder(AiChatService.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .tools(orderQueryTool)
                .contentRetriever(contentRetriever)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
