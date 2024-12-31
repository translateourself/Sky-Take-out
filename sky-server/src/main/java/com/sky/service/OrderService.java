package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 15:31
 */
public interface OrderService {

     OrderSubmitVO submitted(OrdersSubmitDTO ordersDTO);
}
