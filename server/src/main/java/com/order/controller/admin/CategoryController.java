package com.order.controller.admin;

import com.dto.CategoryDTO;
import com.dto.CategoryPageQueryDTO;
import com.entity.Category;
import com.order.service.CategorySerivce;
import com.result.PageResult;
import com.result.Result;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategorySerivce categorySerivce;
    @GetMapping("/list")
    public Result<List> listCategory(Integer type){
        List<Category> categories = categorySerivce.ListCategory(type);
        return Result.success(categories);
    }

    @GetMapping("/page")
    public Result<PageResult> queryPage(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult = categorySerivce.queryPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status")Integer status, Integer id){
        categorySerivce.startOrStop(status,id);
        return Result.success();
    }

    @PutMapping()
    @CacheEvict(cacheNames = {"CategoryCache"},allEntries = true)
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        categorySerivce.updateCategory(categoryDTO);
        return Result.success();
    }

    @DeleteMapping()
    @CacheEvict(cacheNames = {"CategoryCache"},allEntries = true)
    public Result deleteCategory(List<Integer> ids){
        categorySerivce.deleteCategory(ids);
        return Result.success();
    }

}
