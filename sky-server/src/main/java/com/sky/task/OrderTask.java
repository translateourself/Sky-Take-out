package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2025/1/1 14:40
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    

        /**
     * Processes orders that have timed out.
     * This method runs every minute to check for orders that have been in PENDING_PAYMENT status
     * for more than 15 minutes. Such orders are then cancelled.
     *
     * The method performs the following steps:
     * 1. Logs the start of the process.
     * 2. Calculates the cut-off time (15 minutes ago).
     * 3. Retrieves all orders in PENDING_PAYMENT status created before the cut-off time.
     * 4. For each retrieved order:
     *    - Sets the status to CANCELLED.
     *    - Sets the cancel reason to "Order timed out".
     *    - Sets the cancel time to the current time.
     *    - Updates the order in the database.
     *
     * This method is scheduled to run every minute using a cron expression.
     */
    @Scheduled(cron = "0 * * * * ?")

    public void processTimeOutOrders() {
        log.info("the TimeOutOrders is processing...{}",new Date());

        LocalDateTime orderCreateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> timeOutOrders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,orderCreateTime);
        if (timeOutOrders != null && !timeOutOrders.isEmpty()) {
            for (Orders order : timeOutOrders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order timed out");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 * 1 * * ?")
    public void processDeliverOrder(){
        log.info("the DeliverOrder is processing...");

        LocalDateTime orderCreateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> deliverTimeOutOrders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,orderCreateTime);
        if (deliverTimeOutOrders != null && !deliverTimeOutOrders.isEmpty()) {
            deliverTimeOutOrders.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order delivery timed out");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }




    }



}
