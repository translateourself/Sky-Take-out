package com.sky.controller.admin;

import com.google.j2objc.annotations.AutoreleasePool;
import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
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
 * @since 2024/12/27 16:51
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish relate interface")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @PostMapping
    @ApiOperation("Increasing Dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Start increasing Dish");
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
}
