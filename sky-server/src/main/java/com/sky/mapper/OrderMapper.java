package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 15:34
 */
@Mapper
public interface OrderMapper {



    void insert(Orders orders);
}
