package com.sky.mapper;



import com.sky.entity.ChatMemoryStory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 聊天记忆历史表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-15
 */
@Mapper
public interface ChatMemoryStoryMapper{
    @Select("select * from chat_memory_story where memory_id = #{memoryId}")
    ChatMemoryStory selectByMemoryId(String memoryId);

    @Delete("delete from chat_memory_story where memory_id = #{memoryId}")
    int deleteByMemoryId(String memoryId);

    @Update("update chat_memory_story set content = #{content} where memory_id = #{memoryId}")
    int updateContent(ChatMemoryStory chatMemoryStory);

    @Insert("insert into chat_memory_story (memory_id, content, created_at) values (#{memoryId}, #{content}, #{createdAt})")
    int insert(ChatMemoryStory chatMemoryStory);
}
