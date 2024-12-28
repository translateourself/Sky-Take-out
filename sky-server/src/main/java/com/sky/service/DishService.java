package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 16:55
 */
public interface DishService {

    /*
     * function insert one new dish into table
     *
     * @date 2024/12/27 16:56
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /*
     * function DishPage query
     *
     * @date 2024/12/27 19:29
     * @param dishDTO
     * @return com.sky.result.PageResult
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * function: batch delete dish
     *
     * @date 2024/12/27 20:54
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /*
     * function: query dish with flavor
     *
     * @date 2024/12/28 16:42
     * @param dishDTO
     * @return com.sky.vo.DishVO
     */
    DishVO queryDishWithFlavor(Long id);

    /*
     * function update dish and flavor
     *
     * @date 2024/12/28 17:05
     * @param dishDTO
     */
    void updateDishWithFlavor(DishDTO dishDTO);
}
