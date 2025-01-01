package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 21:57
 */
@Mapper
public interface ReportMapper {
    // select sum(amount) from orders where order_time > #{begin} and order_time < #{end}
    public Double sumAccount(Map map);
}
