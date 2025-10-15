package com.sky.chains;

import com.sky.entity.MyChatMemory;
import com.sky.mapper.ChatMemoryMapper;
import dev.langchain4j.data.message.*;
import dev.langchain4j.memory.ChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 弃用
 * 存储时会出现系统文案
 */

public class MysqlChatMemory implements ChatMemory {
    private static final Logger log = LoggerFactory.getLogger(MysqlChatMemory.class);
    private final ChatMemoryMapper chatMemoryMapper;
    private final String sessionId;

    public MysqlChatMemory(String sessionId, ChatMemoryMapper chatMemoryMapper) {
        this.sessionId = sessionId;
        this.chatMemoryMapper = chatMemoryMapper;
    }

    @Override
    public Object id() {
        return sessionId;
    }

    @Override
    public void add(ChatMessage chatMessage) {
        // 不保存 SystemMessage
        if (!(chatMessage instanceof UserMessage || chatMessage instanceof AiMessage)) {
            return;
        }

        MyChatMemory myChatMemory = new MyChatMemory();
        myChatMemory.setSessionId(sessionId);
        myChatMemory.setRole(roleOf(chatMessage));
        myChatMemory.setContent(extractContent(chatMessage));
        chatMemoryMapper.insert(myChatMemory);
    }

    @Override
    public List<ChatMessage> messages() {
        List<Map<String, Object>> rows = chatMemoryMapper.selectBySessionId(sessionId);
        ArrayList<ChatMessage> list = new ArrayList<>();
        System.out.println("=-------------------------" + ChatMessageSerializer.messagesToJson(list));

        for (Map<String, Object> row : rows) {
            String role = (String) row.get("role");
            String content = (String) row.get("content");

            if ("USER".equals(role)) {
                list.add(UserMessage.from(content));
            } else if ("ASSISTANT".equals(role)) {
                list.add(AiMessage.from(content));
            }
        }
        return list;
    }

    @Override
    public void clear() {
        int i = chatMemoryMapper.deleteBySessionId(sessionId);
        if (i < 0) {
            throw new RuntimeException("清空会话失败");
        }
    }

    private String roleOf(ChatMessage message) {
        if (message instanceof UserMessage) return "USER";
        if (message instanceof AiMessage) return "ASSISTANT";
        return "UNKNOWN";
    }

    private String extractContent(ChatMessage chatMessage) {
        if (chatMessage instanceof UserMessage) {
            List<Content> contents = ((UserMessage) chatMessage).contents();
            if (contents != null && !contents.isEmpty() && contents.getFirst() instanceof TextContent) {
                return ((TextContent) contents.getFirst()).text();
            }
            return "";
        }

        if (chatMessage instanceof AiMessage) {
            return ((AiMessage) chatMessage).text();
        }

        return "";
    }





}
