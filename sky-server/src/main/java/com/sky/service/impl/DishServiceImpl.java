package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 16:58
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private FlavorMapper flavorMapper;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //  insert one information into dish table
        dishMapper.insert(dish);

        // get insert sql generated Id
        Long dishId = dish.getId();

        //  insert many flavor data into flavor table

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            flavorMapper.insertBatch(flavors);
        }

    }
}
