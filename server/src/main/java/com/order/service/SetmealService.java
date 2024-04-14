package com.order.service;

import com.dto.SetmealDTO;
import com.dto.SetmealPageQueryDTO;
import com.entity.Setmeal;
import com.result.PageResult;
import com.vo.DishItemVO;
import com.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO setmealById(Integer id);

    void updateSetmeal(SetmealDTO setmealDTO);

    void startOrStopSetmeal(Integer status, Integer id);

    void deleteSetmeal(List<Integer> ids);

    void insertSetmeal(SetmealDTO setmealDTO);

    List<Setmeal> setmealBycategoryId(Integer categoryId);

    List<DishItemVO> getDishById(Integer id);
}
