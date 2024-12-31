package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 15:22
 */
@RestController("userOrderController")
@Api("user operation relative interface")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderservice;

    @PostMapping("/submit")
    @ApiOperation("user submitted order")
    public Result<OrderSubmitVO> submittedOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO OrderSubmitVO = orderservice.submitted(ordersSubmitDTO);
        return Result.success(OrderSubmitVO);
    }

}
