package com.sky.chains;
/*
* 订单查询工具
* */

import com.alibaba.fastjson.JSON;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryTool {
    @Autowired
    private OrderService orderService;

    @Tool(name = "searchOrder", value = "根据用户描述或订单号，提供外卖订单状态查询")
    public String searchOrder(@P(value = "订单id") String id){

        OrderVO orderVO = orderService.getById(Long.valueOf(id));
        if(orderVO == null){
            return "没有此订单";
        }
        return JSON.toJSONString(orderVO);
    }
}
