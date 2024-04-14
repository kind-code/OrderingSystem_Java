package com.order.service;

import com.dto.ShoppingCartDTO;
import com.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCart> getshoppingCartList();

    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    void cleanCart();
}
