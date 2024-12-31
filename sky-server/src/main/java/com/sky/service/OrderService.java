package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 15:31
 */
public interface OrderService {

     OrderSubmitVO submitted(OrdersSubmitDTO ordersDTO);

     /**
      * 订单支付
      * @param ordersPaymentDTO
      * @return
      */
     OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

     /**
      * 支付成功，修改订单状态
      * @param outTradeNo
      */
     void paySuccess(String outTradeNo);
}


