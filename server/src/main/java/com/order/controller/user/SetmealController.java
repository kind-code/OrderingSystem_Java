package com.order.controller.user;

import com.entity.Setmeal;
import com.order.BloomFilter.BloomFilter;
import com.order.service.SetmealService;
import com.result.Result;
import com.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/list")
    @Cacheable(cacheNames = "SetmealCache",key = "#categoryId",unless = "#result==null")
    public Result<List<Setmeal>> setmealList(Integer categoryId){
        if(!BloomFilter.bloomFilter.contains(categoryId)) {
            System.out.println("所要查询的数据既不在缓存中，也不在数据库中，为非法key");
            return null;
        }
        List<Setmeal> setmealList = setmealService.setmealBycategoryId(categoryId);
        return Result.success(setmealList);
    }
    @GetMapping("/dish/{id}")
    @Cacheable(cacheNames = "DishItemVOCache", key="#id",unless = "#result==null")
    public Result<List<DishItemVO>> getDishById(@PathVariable("id") Integer id){
        if(!BloomFilter.bloomFilter.contains(id)) {
            System.out.println("所要查询的数据既不在缓存中，也不在数据库中，为非法key");
            return null;
        }
        List<DishItemVO> dishItemVOList = setmealService.getDishById(id);
        return Result.success(dishItemVOList);
    }
}
