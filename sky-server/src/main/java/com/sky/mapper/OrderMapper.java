package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    //分业查询orders

    List<Orders> page(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(*) from orders where user_id = #{userId}")
    int count(Long userId);

    //查询超时订单
    @Select("select * from orders where status = #{pendingPayment} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLt(Integer pendingPayment, LocalDateTime time);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    @Select("select count(*) from orders")
    int allCount();

    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

    @Select("select * from orders where status = #{status}")
    List<Orders> getByNoSuccess(Integer status);

    //@Select("select sum(amount) from orders where order_time between #{beginTime} and #{endTime} and status = #{status}")
    Double sumByMap(Map map);

    Integer userStatistics(Map map);

    Integer getCount(Map map);
}
