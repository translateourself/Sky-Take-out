package com.sky.controller.user;

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
@RestController("userShopController") // 重命名，防止两个端admin、user的ShopController冲突
@RequestMapping("/user/shop")
@Slf4j
@Api("User Shop relative interface")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY = "SHOP_STATUS";


    // get current status
    @GetMapping("/status")
    @ApiOperation("User get shop current status")
    public Result<Integer> getShopStatus(){
        Integer  status = (Integer)redisTemplate.opsForValue().get(KEY);
        log.info("get shop status, current status:{}",status == 1 ? "Open" : "Close");
        return Result.success(status);

    }
}
