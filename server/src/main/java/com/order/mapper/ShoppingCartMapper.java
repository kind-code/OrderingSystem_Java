package com.order.mapper;

import com.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    @Select("select * from shopping_cart where user_id=#{userid}")
    List<ShoppingCart> queryshoppingCartByUserId(long userid);
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void addShoppingCarts(List<ShoppingCart> shoppingCartList);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES " +
            "(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id=#{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id=#{id}")
    void deleteByUserId(Long id);
}
