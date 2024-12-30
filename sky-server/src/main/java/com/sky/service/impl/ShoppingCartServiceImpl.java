package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/30 22:16
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    // service:
    // Determine whether the items currently added in the shopping cart already exist
    //  if the items is already exist, increase the number of items
    // if the items is not exist, insert a shoppingCart data

    @Override
    public void addGoods(ShoppingCartDTO shoppingCartDTO) {
        // service:
        // Determine whether the items currently added in the shopping cart already exist
        //    sql: select * from shopping_cart where user_id = ? and setmeal_id = ?
        //    sql: select * from shopping_cart where user_id = ? and dish_id = ? and dish_flavor = ?
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long currentUserId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentUserId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //  if the items is already exist, increase the number of items
        if (list != null && !list.isEmpty()) {
           ShoppingCart cart = list.get(0);
           cart.setNumber(cart.getNumber() + 1);
           // update shopping_cart set number = ? where id =?
            shoppingCartMapper.update(cart);

        } else {
            // if the items is not exist, insert a shoppingCart data
            // judge whether the dishes or the setMeal are being added to the shopping cart
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if (dishId != null) {
                // this operation is adding dish to shoppingCart

                Dish dish = dishMapper.queryById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                // this operation is adding setMeal
                Setmeal setMeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setMeal.getName());
                shoppingCart.setImage(setMeal.getImage());
                shoppingCart.setAmount(setMeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }


    /*
     * function : query the shopping cart
     *
     * @date 2024/12/30 23:54
     * @return java.util.List<com.sky.entity.ShoppingCart>
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long currentUserId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(currentUserId).build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /*
     * function : delete the shopping cart
     *
     * @date 2024/12/31 0:02
     */
    @Override
    public void cleanShoppingCart() {
        Long currentUserId = BaseContext.getCurrentId();
        shoppingCartMapper.delete(currentUserId);
    }
}
