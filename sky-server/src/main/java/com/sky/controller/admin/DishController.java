package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 16:51
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "Dish relate interface")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    private DishServiceImpl dishServiceImpl;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("Increasing Dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Start increasing Dish");
        dishService.saveWithFlavor(dishDTO);

        String dishKey = "dish_" + dishDTO.getId();
        cleanCache(dishKey);

        return Result.success();
    }

    /*
     * function : page query
     *
     * @date 2024/12/28 16:36
     * @param dishPageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("Dish page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Page query Dish");
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /*
     * function: Delete Batch dish
     *
     * @date 2024/12/28 16:36
     * @param ids
     * @return com.sky.result.Result
     */
    @DeleteMapping
    @ApiOperation("Delete Batch Dish")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("Batch Delete Dish :{}",ids);
        dishService.deleteBatch(ids);

        cleanCache("dish_*");
        return Result.success();
    }


    /*
     * function query dish and flavor
     *
     * @date 2024/12/28 16:58
     * @param dishDTO
     * @return com.sky.result.Result<com.sky.vo.DishVO>
     */
    @GetMapping("/{id}")
    @ApiOperation("Query dish and flavor by id")
    public Result<DishVO> queryDishWithFlavor(@PathVariable Long id) {
        DishVO queryResult = dishService.queryDishWithFlavor(id);
        return Result.success(queryResult);
    }

    @PutMapping
    @ApiOperation("Modified dish with flavor")
    public Result updateDishWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("modify info:{}",dishDTO);
        dishService.updateDishWithFlavor(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据id修改起售停售状态
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result onOff(@PathVariable Integer status,Long id){
        log.info("根据id修改状态，{}", id);
        dishService.onOff(id);
        // 将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    // clean up the cache
    private void cleanCache(String pattern){
        // query all key which the prefix is patter
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
