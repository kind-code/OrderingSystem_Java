package com.order.controller.admin;

import com.dto.SetmealDTO;
import com.dto.SetmealPageQueryDTO;
import com.order.service.SetmealService;
import com.result.PageResult;
import com.result.Result;
import com.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/page")
    public Result<PageResult> QueryPage(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.queryPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> setmealById(@PathVariable("id") Integer id){
        SetmealVO setmealVO = setmealService.setmealById(id);
        return Result.success(setmealVO);
    }

    @PutMapping()
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache"},allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改信息为{}",setmealDTO.toString());
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache"},allEntries = true)
    public Result startOrStopSetmeal(@PathVariable("status") Integer status, Integer id){
        log.info("改变状态{}",status);
        setmealService.startOrStopSetmeal(status,id);
        return Result.success();
    }

    @DeleteMapping()
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache"},allEntries = true)
    public Result deleteSetmeal(List<Integer> ids){
        log.info("批量删除套餐{}",ids.toString());
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }
    @PostMapping()
    @CacheEvict(cacheNames = {"setmealCache","DishItemVOCache"},allEntries = true)
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.insertSetmeal(setmealDTO);
        return Result.success();
    }
}
