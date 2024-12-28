package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.google.j2objc.annotations.AutoreleasePool;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 16:51
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish relate interface")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    private DishServiceImpl dishServiceImpl;

    @PostMapping
    @ApiOperation("Increasing Dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Start increasing Dish");
        dishService.saveWithFlavor(dishDTO);
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
        return Result.success();
    }

}
