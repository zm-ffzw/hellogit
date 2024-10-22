package com.sky.task;

import com.alibaba.fastjson.JSON;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;
    //订单超时响应
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        //待支付时间 < 当前时间 - 15min = 超时
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //检查未支付的订单，并且15分钟内未支付
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLt(Orders.PENDING_PAYMENT,time);

        if(list != null && list.size() > 0){
            for(Orders order : list){
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
    //处理派送中的订单
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder(){
        //待支付时间 < 当前时间 - 60min = 上个工作日派送中订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        //检查未支付的订单，并且15分钟内未支付
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLt(Orders.DELIVERY_IN_PROGRESS,time);

        if(list != null && list.size() > 0){
            for(Orders order : list){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "*/10 * * * * *")
    public void pySuccess(){

        //查询未支付
        List<Orders> list = orderMapper.getByNoSuccess(Orders.PENDING_PAYMENT);

        //修改状态，并且发送消息
        if(list != null && list.size() > 0){
            for(Orders order : list){
                order.setStatus(Orders.TO_BE_CONFIRMED);
                orderMapper.update(order);

                //通过websocket推送·消息
                Map map = new HashMap();
                map.put("type",1);//1表示来单提醒，2客户催单
                map.put("orderId",order.getId());
                map.put("content","订单号："+order.getNumber());
                Object json = JSON.toJSON(map);
                webSocketServer.sendToAllClient(json.toString());
            }
        }
    }
}
