package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WorkspaceService workspaceService;
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
            map.put("begin", beginTime);
            map.put("end", endTime);
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

    //数据报表导出
    public void exportBusinessData(HttpServletResponse response) {
        //获取时间
        LocalDate beginTime = LocalDate.now().plusDays(-30);
        LocalDate endTime = LocalDate.now().plusDays(-1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginTime, LocalTime.MIN), LocalDateTime.of(endTime, LocalTime.MAX));

        //填入报表
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //基于模版，创建一个excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);
            //获取当前业
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //填充数据
            sheet.getRow(1).getCell(1).setCellValue("时间："+beginTime+"至"+endTime);
            //获取4,5行
            sheet.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());

            //明细数据写入
            for (int i = 0; i < 30; i++){
                LocalDate date = beginTime.plusDays(i);
                LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
                BusinessDataVO businessData1 = workspaceService.getBusinessData(begin, end);

                XSSFRow sheetRow = sheet.getRow(7 + i);
                sheetRow.getCell(1).setCellValue(date.toString());
                sheetRow.getCell(2).setCellValue(businessData1.getTurnover());
                sheetRow.getCell(3).setCellValue(businessData1.getValidOrderCount());
                sheetRow.getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                sheetRow.getCell(5).setCellValue(businessData1.getUnitPrice());
                sheetRow.getCell(6).setCellValue(businessData1.getNewUsers());
            }
            //通过数出流将文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关流
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
