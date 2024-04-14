package com.order.controller.user;

import com.dto.ShoppingCartDTO;
import com.entity.ShoppingCart;
import com.order.service.ShoppingCartService;
import com.result.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoppingCart(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.getshoppingCartList();
        return Result.success(shoppingCartList);
    }
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @DeleteMapping("/clean")
    public Result cleanCart(){
        shoppingCartService.cleanCart();
        return Result.success();
    }
}
