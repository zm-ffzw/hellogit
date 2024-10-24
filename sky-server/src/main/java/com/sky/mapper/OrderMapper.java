package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    @Select("select od.name,sum(od.number) number from orders o left join order_detail od on o.id = od.order_id " +
            "where o.order_time between #{begin} and #{end} and status = #{status} " +
            "group by od.name order by number desc limit 10")
    List<GoodsSalesDTO> getSalesTop10(LocalDate begin, LocalDate end, Integer status);
}
