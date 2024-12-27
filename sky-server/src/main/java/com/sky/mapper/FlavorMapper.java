package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlavorMapper {

    //  batch insert flavor data

    void insertBatch(List<DishFlavor> flavors);

    // delete flavor according to dish_id in dish_flavor
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteFlavorById(Long dishId);


    void deleteFlavorByIds(List<Long> dishId);
}
