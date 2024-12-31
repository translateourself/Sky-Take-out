package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 16:54
 */
@Mapper
public interface OrderDetailMapper {

    public void insertBatch(List<OrderDetail> orderDetails);
}
