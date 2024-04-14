package com.order.controller.admin;

import com.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class ShopController {
    public static Integer status = 1;
    public static final String KEY = "SHOP_STATUS";
    //    @Cacheable(value = KEY,key ="#status")
    @GetMapping("/status")
    public Result shopStataus(){
        Integer status = this.status;
        return Result.success(status);
    }
    @PutMapping("/{status}")
//    @CachePut(value = KEY,key = "#status")
    public Result SetShopStatus(@PathVariable("status") Integer status){
        this.status = status;
        return Result.success();
    }
}
