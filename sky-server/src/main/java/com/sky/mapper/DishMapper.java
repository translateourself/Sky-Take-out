package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    //query dish entity according to ID
    @Select("select * from dish where id= #{id}")
    Dish queryById(long id);

    @Delete("delete from dish where id = #{id}")
    void deleteDishById(Long id);

    /*
     * function : batch delete dish according to Dish
     *
     * @date 2024/12/27 21:56
     * @param ids
     * sql:delete  from dish where id in (1,2,3)
     */
    void deleteDishByIds(List<Long> ids);

    /*
     * function dynamic modify dish
     *
     * @date 2024/12/28 17:08
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    List<Dish> getBySetmealId(Long id);


    @Update("update dish set status = IF(status = 0, 1, 0) where id = #{id}")
    void onOff(Long id);

    @Mapper

        /**
         * 根据条件统计菜品数量
         * @param map
         * @return
         */
        Integer countByMap(Map map);

}
