package com.order.mapper;

import com.dto.CategoryDTO;
import com.dto.CategoryPageQueryDTO;
import com.entity.Category;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> queryCategory(Integer type);

    Page<Category> queryPage(CategoryPageQueryDTO categoryPageQueryDTO);
    @Update("update category set status=#{status} where id = #{id}")
    void startOrStopStatusById(Integer status, Integer id);

    void updateCategory(CategoryDTO categoryDTO);

    void deleteByIds(List<Integer> ids);
}
