package com.sky.controller.admin;

import com.sky.chains.MysqlChatMemory;
import com.sky.result.Result;
import com.sky.service.AiChatService;
import dev.langchain4j.data.message.ChatMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI客服")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/chatStream/{sessionId}")
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestParam String message,
            @PathVariable String sessionId) {

        return aiChatService
                .chatStream(message, "1")
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build()
                );
    }

    /*@Operation(summary = "查询历史对话")
    @PostMapping("/chat/{sessionId}")
    public Result chat(String message, @PathVariable String sessionId){
        List<ChatMessage> chatMessageList = mysqlChatMemory.messages();
        return Result.success(chatMessageList);
    }

    @Operation(summary = "添加对话")
    @PostMapping("/chatAdd/{sessionId}")
    public Result chatAdd(String message, @PathVariable String sessionId){


        mysqlChatMemory.add();
        return Result.success();
    }*/

}
