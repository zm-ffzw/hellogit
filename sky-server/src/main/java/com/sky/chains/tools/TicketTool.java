package com.sky.chains.tools;

import com.sky.context.BaseContext;
import com.sky.entity.Ticket;
import com.sky.mapper.TicketMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 工单记录
 */
@Component
@Slf4j
public class TicketTool {
    @Autowired
    private TicketMapper ticketMapper;

    @Tool("记录用户投诉/问题工单，参数为用户原始内容，并写入MySQL")
    public String createTicket(@P("用户原始内容") String content){
        Ticket ticket = new Ticket();
        ticket.setContent(content);
        ticket.setStatus("PENDING");
        ticket.setCreateTime(LocalDateTime.now());
        ticket.setEmployeeId(1L);
        ticketMapper.insert(ticket);

        log.info("工单创建成功，用户id：{}，工单编号为：{}",ticket.getEmployeeId(), ticket.getId());

        return "工单创建成功，工单编号为：" + ticket.getId();
    }
}
