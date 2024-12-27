package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 21:16
 */
@Mapper
public interface SetMealDishMapper {
    // select  id from setmeal_dish where dish id in (1,2,3,4)
    List<Long> getSetMealDishByDishId(List<Long> dishIds);
}
