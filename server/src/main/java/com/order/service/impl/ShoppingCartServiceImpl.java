package com.order.service.impl;

import com.context.BaseContext;
import com.dto.ShoppingCartDTO;
import com.entity.Dish;
import com.entity.Setmeal;
import com.entity.ShoppingCart;
import com.order.mapper.DishMapper;
import com.order.mapper.SetmealMapper;
import com.order.mapper.ShoppingCartMapper;
import com.order.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public List<ShoppingCart> getshoppingCartList() {
        long userid = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.queryshoppingCartByUserId(userid);
        return shoppingCartList;
    }

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        long userId = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list!=null && list.size()>0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        }else { //如果不存在，就插入一条购物车数据

            // 判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCart.getDishId();
            if(dishId!=null){
                // 本次添加的是菜品
                Dish dish = dishMapper.searchDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else {
                //本次添加的是套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.querySetmealById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    @Override
    public void cleanCart() {
        long userId = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }
}
