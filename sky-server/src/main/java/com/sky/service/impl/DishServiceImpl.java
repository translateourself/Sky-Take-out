package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    @Autowired
    private SetMealDishMapper setMealDishMapper;

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

    //
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page  = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }


    /*
     * function : batch delete dish and relation flavor.
     *
     * @date 2024/12/27 21:39
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // according condition to delete dish
        //1. this dish can't delete which the status is in sale
        //query dish status according ids
        for (long id : ids) {
            Dish dish = dishMapper.queryById(id);
            if (dish.getStatus() != StatusConstant.DISABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //2. this dish can't delete which be nested with set meal
        List<Long> nestMealDish = setMealDishMapper.getSetMealDishByDishId(ids);
        if (nestMealDish !=null && !nestMealDish.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //3. delete the dish meanwhile delete nested flavor
//        for (long id : ids){
//            dishMapper.deleteDishById(id);
//            // note : this id is dish id
//            flavorMapper.deleteFlavorById(id);
//        }
        //batch delete
        //sql: delete from dish  where id in (1,2,3,4)
        dishMapper.deleteDishByIds(ids);

        //sql: delete from dish_flavor where dish_id in (1,2,3,4)
        flavorMapper.deleteFlavorByIds(ids);
    }

    @Override
    public DishVO queryDishWithFlavor(Long id) {
        // query dish according to id in dish table
        Dish dish = dishMapper.queryById(id);

        // query flavor according to dish_id in dish_flavor table
        List<DishFlavor> flavors = flavorMapper.queryByDishId(id);

        //  encapsulate data to result type
        DishVO resultVo = new DishVO();
        BeanUtils.copyProperties(dish,resultVo);
        resultVo.setFlavors(flavors);
        return resultVo;
    }

    @Override
    public void updateDishWithFlavor(DishDTO dishDTO) {
        //1.  update dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        //2. update flavor:  delete current flavor then insert new flavor

        flavorMapper.deleteFlavorById(dishDTO.getId());

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
