package com.sky.chains;

import com.sky.entity.ChatMemoryStory;
import com.sky.mapper.ChatMemoryStoryMapper;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义向量数据库
 */
@Component
public class MysqlChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private ChatMemoryStoryMapper chatMemoryStoryMapper;

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {

        ChatMemoryStory chatMemory = chatMemoryStoryMapper.selectByMemoryId((String) memoryId);
        if(chatMemory != null){
            chatMemoryStoryMapper.updateContent(getChatMemoryStory(memoryId, messages));
        }else {
            ChatMemoryStory chatMemoryStory = getChatMemoryStory(memoryId, messages);
            chatMemoryStory.setCreatedAt(LocalDateTime.now());
            chatMemoryStoryMapper.insert(chatMemoryStory);
        }
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        ChatMemoryStory chatMessageStory = chatMemoryStoryMapper.selectByMemoryId((String) memoryId);
        if(chatMessageStory == null){
            return new LinkedList<>();
        }
        //获取json字符串的消息历史
        String content = chatMessageStory.getContent();
        //将json字符串转换成消息列表list<chatMessage>
        return ChatMessageDeserializer.messagesFromJson(content);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        int i = chatMemoryStoryMapper.deleteByMemoryId((String) memoryId);
    }

    private ChatMemoryStory getChatMemoryStory(Object memoryId, List<ChatMessage> messages){
        ChatMemoryStory chatMemoryStory = new ChatMemoryStory();
        chatMemoryStory.setMemoryId((String) memoryId);
        chatMemoryStory.setContent(ChatMessageSerializer.messagesToJson(messages));
        return chatMemoryStory;
    }


}
