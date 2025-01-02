package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:48
 */
public interface ReportService {
     TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);

     UserReportVO getUserStatics(LocalDate begin, LocalDate end);

     OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

     SalesTop10ReportVO getSalesTop10ReportVO(LocalDate begin, LocalDate end);

     void exportDataToExcel(HttpServletResponse response);
}
