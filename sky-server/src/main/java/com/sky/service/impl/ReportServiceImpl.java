package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:55
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    WorkspaceService workspaceService;

    /*
     * function : Statistics of the turnover account with the specified time range
     *
     * @date 2025/1/1 21:56
     * @param begin
     * @param end
     * @return com.sky.vo.TurnoverReportVO
     */
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> datesList = new ArrayList<>();
        datesList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            datesList.add(begin);
        }
        ArrayList<Double> turnOverList = new ArrayList<>();
        for (LocalDate date : datesList){
            //查询date日期对应的营业额数据，营业额是指：状态为“已完成”的订单金额合计。
            //LocalDate只有年月日
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
           Map map = new HashMap<>();
           map.put("begin", beginTime);
           map.put("end", endTime);
           map.put("status", Orders.COMPLETED);

          Double account = reportMapper.sumAccount(map);
        // if current account is null, set it to 0.0
          account = account == null ? 0.0 : account;
          turnOverList.add(account);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(datesList,","))
                .turnoverList(StringUtils.join(turnOverList,","))
                .build();
    }

    @Override
    public UserReportVO getUserStatics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> datesList = new ArrayList<>();
        datesList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            datesList.add(begin);
        }
        // 存放每天的新增用户数量 select count(id) from user where create_time < ? and create_time> ?
        ArrayList<Integer> newUserList = new ArrayList<>();
        //  存放每天的总用户数量 select count(id) from user where create_time < ?
        ArrayList<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : datesList){
            //LocalDate只有年月日
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            // query total user count just needed one parameter :end
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);

            // query new user count  needed another parameter :begin
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);


            // if current totalUser is null, set it to 0.0
            totalUser = totalUser == null ? 0 : totalUser;
            newUser = newUser == null ? 0 : newUser;

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(datesList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }


    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> datesList = new ArrayList<>();
        datesList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            datesList.add(begin);
        }

        // 存放每天的orders number  select count(id) from user where create_time < ? and create_time> ?
        ArrayList<Integer> totalOrderList = new ArrayList<>();
        //  存放每天的有效订单数 select count(id) from order where create_time < ?

        ArrayList<Integer> totalValidList = new ArrayList<>();
        for (LocalDate date : datesList){

            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap<>();
            // query day order count  needed two parameter :begin and end
            map.put("end", endTime);
            map.put("begin", beginTime);
            Integer dailyTotalOrder = orderMapper.countOrdersByMap(map);

            // query day order count  needed two parameter :begin , end and status = 5
            map.put("status", Orders.COMPLETED);
            Integer dailyValidOrder = orderMapper.countOrdersByMap(map);

            totalOrderList.add(dailyTotalOrder);
            totalValidList.add(dailyValidOrder);
        }
        // calculate the  total order count during the specified time range
        Integer totalOrderCount = totalOrderList.stream().reduce(Integer::sum).get();
        // calculate the  valid order count during the specified time range
        Integer totalValidCount = totalValidList.stream().reduce(Integer::sum).get();

        //calculate the  percentage of valid orders to total orders
        Double validPercentage = 0.0;
        if (totalOrderCount != 0) {
            validPercentage = totalValidCount.doubleValue() / totalOrderCount;
        }


        return OrderReportVO.builder()
                .dateList(StringUtils.join(datesList,","))
                .orderCountList((StringUtils.join(totalOrderList,",")))
                .validOrderCountList((StringUtils.join(totalValidList,",")))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidCount)
                .orderCompletionRate(validPercentage)
                .build();


    }

    @Override
    public SalesTop10ReportVO getSalesTop10ReportVO(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO>  salesTop10 = orderMapper.getSalesTop10(beginTime,endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> number = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        String nameList = StringUtils.join(names,",");
        String numberList = StringUtils.join(number,",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    @Override
    public void exportDataToExcel(HttpServletResponse response) {
        //1. query the database to get the business data
        LocalDate dateBegin = LocalDate.now().minusDays(3); //set the date before 30 days
        LocalDate dateEnd = LocalDate.now();
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        // write business data to excel file using Apache POI library
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //  create a Excel file based on template Excel file
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // file data time
            sheet.getRow(1).getCell(1).setCellValue("时间" + dateBegin + "至" + dateEnd);
            // get the fourth row
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            // get the fifth row
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            // detail filled
            for (int i = 0 ; i < 3; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //  query certain day's data
                BusinessDataVO data = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                // get certain row
                row = sheet.getRow(i + 7);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(data.getTurnover());
                row.getCell(3).setCellValue(data.getValidOrderCount());
                row.getCell(4).setCellValue(data.getOrderCompletionRate());
                row.getCell(5).setCellValue(data.getUnitPrice());
                row.getCell(6).setCellValue(data.getNewUsers());
            }

            // pass the excel file to bowser for download through outPutStream
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // close
            out.close();
            excel.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
