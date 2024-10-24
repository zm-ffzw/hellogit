package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    //营业饿统计
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }

        List<Double> amountList = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            //select sum(amount) from orders where between beginTime and endTime and status = ....
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            amountList.add(turnover);
        }

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(StringUtils.join(localDateList, ","))
                .turnoverList(StringUtils.join(amountList, ","))
                .build();

        return turnoverReportVO;
    }

    //用户
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //查询日期
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }

        //当天用户人数：select * from users where begin > orderTime and end .....
        List<Integer> newUserList = new ArrayList<>();
        //总用户人数：select * from users where end > orderTime
        List<Integer> totalUserList = new ArrayList<>();

        //循环每天查询对应人数
        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end",endTime);

            Integer totalUser = orderMapper.userStatistics(map);
            totalUserList.add(totalUser);

            map.put("begin",beginTime);
            Integer newUser = orderMapper.userStatistics(map);
            newUserList.add(newUser);
        }
        //封装对象
        return UserReportVO.builder()
                .dateList(StringUtils.join(localDateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    //订单统计
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        Integer totalOrderCount = orderMapper.getCount(map);
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.getCount(map);

        //订单完成率
        double orderCompletionRate = 0.0;
        if (totalOrderCount != 0){
            orderCompletionRate = (double) validOrderCount / (double)totalOrderCount;
        }
        //查询日期
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }

        //有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();
        //总订单数
        List<Integer> orderCountList = new ArrayList<>();

        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);

            Integer orderCount = orderMapper.getCount(map);
            orderCountList.add(orderCount);

            map.put("status", Orders.COMPLETED);
            Integer validOrder = orderMapper.getCount(map);
            validOrderCountList.add(validOrder);
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(localDateList,","))
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .build();
    }

    //top10
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();

        //商品名称
        List<String> nameList = new ArrayList<>();
        //销量
        List<Integer> numberList = new ArrayList<>();

        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(begin,end, Orders.COMPLETED);

        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            nameList.add(goodsSalesDTO.getName());
            numberList.add(goodsSalesDTO.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
}
