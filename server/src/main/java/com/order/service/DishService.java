package com.order.service;

import com.dto.DishDTO;
import com.dto.DishPageQueryDTO;
import com.entity.Dish;
import com.result.PageResult;
import com.vo.DishVO;

import java.util.List;

public interface DishService {
    PageResult queryPage(DishPageQueryDTO dishPageQueryDTO);

    void changeStatus(Integer status,Long id);

    List<Dish> searchDishByCateId(Integer categoryId);

    DishVO searchDishById(Integer id);

    void insertDish(DishDTO dishDTO);

    void updateDish(DishDTO dishDTO);

    void deleteDish(List<Integer> ids);

    List<DishVO> dishVOBycategoryId(Integer categoryId);
}
