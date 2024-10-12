package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
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
}
