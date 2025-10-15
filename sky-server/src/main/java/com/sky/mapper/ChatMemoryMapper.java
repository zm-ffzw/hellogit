package com.sky.mapper;

import com.sky.entity.MyChatMemory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-11
 */
@Mapper
public interface ChatMemoryMapper{

    @Insert("insert into chat_memory (session_id, role, content) VALUES (#{sessionId}, #{role}, #{content})")
    public int insert(MyChatMemory myChatMemory);

    @Select("SELECT role, content FROM chat_memory WHERE session_id = #{sessionId} ORDER BY id ASC")
    public List<Map<String, Object>> selectBySessionId(String sessionId);

    @Delete("DELETE FROM chat_memory WHERE session_id = #{sessionId}")
    public int deleteBySessionId(String sessionId);

}
