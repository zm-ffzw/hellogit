package com.sky.config;

import com.sky.chains.MysqlChatMemory;
import com.sky.chains.MysqlChatMemoryStore;
import com.sky.chains.tools.EmotionTool;
import com.sky.chains.tools.OrderQueryTool;
import com.sky.chains.tools.TicketTool;
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
    @Autowired
    private EmotionTool emotionTool;
    @Autowired
    private TicketTool ticketTool;
    @Autowired
    private MysqlChatMemoryStore mysqlChatMemoryStore;


    @Bean
    public AiChatService aiChatService() {
        //创建会话提供者
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(mysqlChatMemoryStore)
                .build();
        return AiServices.builder(AiChatService.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .tools(orderQueryTool, emotionTool, ticketTool)
                .contentRetriever(contentRetriever)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
