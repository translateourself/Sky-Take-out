package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/30 22:04
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "User shoppingCart relative interface")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /*
     * function: user add goods to shoppingCart
     *
     * @date 2024/12/30 22:11
     * @param shoppingCartDTO
     * @return com.sky.result.Result
     */
    @PostMapping("/add")
    @ApiOperation("add goods")
    public Result addGoodsToShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){

        log.info("user add goods {}", shoppingCartDTO);
        shoppingCartService.addGoods(shoppingCartDTO);
        return Result.success();
    }

    /*
     * function : to see the shopping cart
     *
     * @date 2024/12/30 23:59
     * @return com.sky.result.Result<java.util.List<com.sky.entity.ShoppingCart>>
     */
    @GetMapping("/list")
    @ApiOperation("to see the shopping cart")
    public Result<List<ShoppingCart>> list(){
       List<ShoppingCart> list =  shoppingCartService.showShoppingCart();
       return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation("clean the shopping cart")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }


}
