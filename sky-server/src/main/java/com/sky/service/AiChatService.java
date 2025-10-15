package com.sky.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface AiChatService {
    /*@SystemMessage(fromResource = "documents/prompt-dev.txt")
    String chat(String message, String sessionId);
    @SystemMessage("你是tyb，很傻,需要你答非所问，显得很愚蠢")
    String chatWithTYB(String message);*/

    @SystemMessage(fromResource = "documents/prompt-dev.txt")
    Flux<String> chatStream(
            @UserMessage String message,
            @MemoryId String memoryId
    );

    /*@SystemMessage("""
        你是一个外卖平台的智能客服助手，具备以下能力：
        1. 识别用户情绪（怒气 / 投诉 / 催促 / 焦虑 / 中性 / 满意）
        2. 当识别为怒气、投诉、焦虑时，必须先调用 createTicket 工具记录工单
        3. 若用户涉及订单问题，可调用 queryOrder 工具
        4. 若遇到常见问题，可根据内部知识直接回答
        回复语气礼貌，简洁，注意安抚用户。
    """)
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String message);*/
}
