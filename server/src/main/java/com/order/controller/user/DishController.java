package com.order.controller.user;

import com.entity.Dish;
import com.order.BloomFilter.BloomFilter;
import com.order.service.DishService;
import com.result.Result;
import com.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping("/list")
    @Cacheable(cacheNames = "DishVoCache",key = "#categoryId" ,unless = "#result==null")
    public Result<List<DishVO>> dishList(Integer categoryId){
        if(!BloomFilter.bloomFilter.contains(categoryId)) {
            System.out.println("所要查询的数据既不在缓存中，也不在数据库中，为非法key");
            return null;
        }
        List<DishVO> dishVOList = dishService.dishVOBycategoryId(categoryId);
        return Result.success(dishVOList);
    }

}
