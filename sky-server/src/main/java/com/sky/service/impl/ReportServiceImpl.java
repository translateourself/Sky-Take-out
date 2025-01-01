package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.catalina.mapper.Mapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:55
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

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
}
