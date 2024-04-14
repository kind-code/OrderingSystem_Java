package com.order.service;

import com.dto.CategoryDTO;
import com.dto.CategoryPageQueryDTO;
import com.entity.Category;
import com.result.PageResult;

import java.util.List;

public interface CategorySerivce {
    List<Category> ListCategory(Integer type);

    PageResult queryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    void startOrStop(Integer status, Integer id);

    void updateCategory(CategoryDTO categoryDTO);

    void deleteCategory(List<Integer> ids);
}
