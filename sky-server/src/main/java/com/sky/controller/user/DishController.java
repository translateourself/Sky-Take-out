package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        //  query redis if it exist dish data
            // convention : we put the dish_ + dynamic category id to generate key
        String redisKey = "dish_" + categoryId;
        List<DishVO> queryDishList =  (List<DishVO>) redisTemplate.opsForValue().get(redisKey);
        //  get value by redis if it existed;
        if (queryDishList != null && !queryDishList.isEmpty()){

            return Result.success(queryDishList);
        }
       // if the data don't exist;

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        queryDishList = dishService.listWithFlavor(dish);
        //  save the dish data in redis if it don't exist;
        redisTemplate.opsForValue().set(redisKey,queryDishList);

        return Result.success(queryDishList);
    }

}
