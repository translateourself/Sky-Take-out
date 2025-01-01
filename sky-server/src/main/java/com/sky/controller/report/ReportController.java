package com.sky.controller.report;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:46
 */
@RestController
@Api(tags = "report relative interface")
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;


    /*
     * function :   Statistics of the turnover account with the specified time range
     *
     * @date 2025/1/1 22:25
     * @param begin
     * @param end
     * @return com.sky.result.Result<com.sky.vo.TurnoverReportVO>
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("turnoverReport, begin: {}, end: {}", begin, end);
        TurnoverReportVO turnoverReportVO  = reportService.getTurnOverStatistics(begin, end);
        return Result.success(turnoverReportVO);

    }
}
