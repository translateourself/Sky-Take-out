package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

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

     /**
      * 用户端订单分页查询
      * @param page
      * @param pageSize
      * @param status
      * @return
      */
     PageResult pageQuery4User(int page, int pageSize, Integer status);

     /**
      * 查询订单详情
      * @param id
      * @return
      */
     OrderVO details(Long id);

     /**
      * 用户取消订单
      * @param id
      */
     void userCancelById(Long id) throws Exception;

     /**
      * 再来一单
      *
      * @param id
      */
     void repetition(Long id);

     PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

     OrderStatisticsVO statistics();

     void confirm(OrdersCancelDTO ordersCancelDTO);

     void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

     void cancel(OrdersCancelDTO ordersCancelDTO)  throws Exception;

     void delivery(Long id);

     void complete(Long id);

     void reminder(Long id);
}


