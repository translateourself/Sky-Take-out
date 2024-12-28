package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuxunne
 * @description:  shop operation
 * @since 2024/12/28 20:51
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api("Shop relative interface")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY = "SHOP_STATUS";

    /*
     * function: operate shop open or close
     *
     * @date 2024/12/28 20:56
     * @param status
     * @return com.sky.result.Result
     */
    @PutMapping("/{status}")
    @ApiOperation("Admin operation shop status")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("set shop status, current status:{}",status == 1 ? "Open" : "Close");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    // get current status
    @GetMapping("/status")
    @ApiOperation("Admin get shop current status")
    public Result<Integer> getShopStatus(){
        Integer  status = (Integer)redisTemplate.opsForValue().get(KEY);
        log.info("get shop status, current status:{}",status == 1 ? "Open" : "Close");
        return Result.success(status);

    }
}
