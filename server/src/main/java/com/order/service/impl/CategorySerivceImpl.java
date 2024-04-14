package com.order.service.impl;

import com.dto.CategoryDTO;
import com.dto.CategoryPageQueryDTO;
import com.entity.Category;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.order.mapper.CategoryMapper;
import com.order.mapper.DishMapper;
import com.order.service.CategorySerivce;
import com.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategorySerivceImpl implements CategorySerivce {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    public List<Category> ListCategory(Integer type) {
        List<Category> categories = categoryMapper.queryCategory(type);
        return categories;
    }

    @Override
    public PageResult queryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> categories = categoryMapper.queryPage(categoryPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(categories.getTotal());
        pageResult.setRecords(categories.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void startOrStop(Integer status, Integer id) {
        //禁用相关菜品
        dishMapper.startOrStopStatusByCategoryId(status,id);
        //禁用分类
        categoryMapper.startOrStopStatusById(status,id);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        categoryMapper.updateCategory(categoryDTO);
    }

    @Override
    public void deleteCategory(List<Integer> ids) {
        dishMapper.deleteDishByCategoryIds(ids);
        categoryMapper.deleteByIds(ids);
    }
}
