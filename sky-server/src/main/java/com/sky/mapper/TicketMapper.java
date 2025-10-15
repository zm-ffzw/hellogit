package com.sky.mapper;

import com.sky.entity.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface TicketMapper {

    @Insert("""
        INSERT INTO ticket(content, status, create_time,employee_id)
        VALUES(#{content}, #{status}, #{createTime},#{employeeId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Ticket ticket);
}
