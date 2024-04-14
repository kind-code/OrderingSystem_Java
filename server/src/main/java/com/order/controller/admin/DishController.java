package com.order.controller.admin;

import com.dto.DishDTO;
import com.dto.DishPageQueryDTO;
import com.entity.Dish;
import com.order.BloomFilter.BloomFilter;
import com.order.service.DishService;
import com.result.PageResult;
import com.result.Result;
import com.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public Result<PageResult> queryPage(DishPageQueryDTO dishPageQueryDTO){
        log.info("显示菜品信息");
        PageResult pageResult = dishService.queryPage(dishPageQueryDTO);
        return Result.success(pageResult);

    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache","DishVoCache"},allEntries = true)
    public Result startOrStop(@PathVariable("status")Integer status,long id){
        dishService.changeStatus(status,id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List> searchDishByCateId(Integer categoryId){
        log.info("根据菜品类别查询信息:{}",categoryId);
        List<Dish> dish = dishService.searchDishByCateId(categoryId);
        return Result.success(dish);
    }

    @GetMapping("/{id}")
    public Result<DishVO> searchDishById(@PathVariable("id")Integer id){
        log.info("菜品id:{}",id);
        DishVO dishVO = dishService.searchDishById(id);
        return Result.success(dishVO);
    }

    @PostMapping()
    @CacheEvict(cacheNames = {"DishVoCache"},allEntries = true)
    public Result insertDish(@RequestBody DishDTO dishDTO){
        dishService.insertDish(dishDTO);
        return Result.success();
    }

    @PutMapping()
    @CacheEvict(cacheNames = {"DishItemVOCache","DishVoCache"},allEntries = true)
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改信息为{}",dishDTO.toString());
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping()
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache","DishVoCache"},allEntries = true)
    public Result DeleteDish(List<Integer> ids){
        log.info("删除菜品id{}",ids.toString());
        dishService.deleteDish(ids);
        return Result.success();
    }



}
