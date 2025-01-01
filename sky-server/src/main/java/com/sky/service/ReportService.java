package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:48
 */
public interface ReportService {
     TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);
}
