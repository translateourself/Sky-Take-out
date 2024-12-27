package com.sky.service;

import com.sky.dto.DishDTO;

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
}
