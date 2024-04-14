package com.order.controller.user;

import com.entity.Category;
import com.order.service.CategorySerivce;
import com.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {
    @Autowired
    private CategorySerivce categorySerivce;
    @GetMapping("/list")
//    @Cacheable(cacheNames = "CategoryCache",key = "#type")
    public Result<List<Category>> categoryList(Integer type){
        List<Category> categories = categorySerivce.ListCategory(type);
        return Result.success(categories);
    }



}
