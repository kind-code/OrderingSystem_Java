package com.order.service.impl;

import com.constant.StatusConstant;
import com.dto.DishDTO;
import com.dto.DishPageQueryDTO;
import com.entity.Dish;
import com.entity.DishFlavor;
import com.entity.SetmealDish;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.order.BloomFilter.BloomFilter;
import com.order.mapper.DishMapper;
import com.order.mapper.SetmealMapper;
import com.order.service.DishService;
import com.result.PageResult;
import com.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public PageResult queryPage(DishPageQueryDTO dishPageQueryDTO) {
        //查询页码数(必填)  每页记录数(可选，默认 10)
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<Dish> dishPage = dishMapper.queryPage(dishPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setRecords(dishPage.getResult());
        pageResult.setTotal(dishPage.getTotal());
        return pageResult;
    }

    @Override
    @Transactional
    public void changeStatus(Integer status,Long id) {
        if(status == StatusConstant.DISABLE)
        {
           List<Integer> setmeals = setmealMapper.querySetmealByDish(id);
           setmeals.forEach(setmeal->{
               setmealMapper.startOrStopSetmeal(status,setmeal);
           });
            dishMapper.startOrStopStatus(status,id);
        }
        else if (status == StatusConstant.ENABLE) {
            dishMapper.startOrStopStatus(status,id);
        }
    }

    @Override
    public List<Dish> searchDishByCateId(Integer categoryId) {
        List<Dish> dish = dishMapper.searchDishByCategoryId(categoryId);
        return dish;
    }

    @Override
    public DishVO searchDishById(Integer id) {
        Dish dish = dishMapper.searchDishById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        List<DishFlavor> flavors = dishMapper.searchDishFlavorsByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    public void insertDish(DishDTO dishDTO) {
        List<DishFlavor> dishFlavor = dishDTO.getFlavors();
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insertDish(dish);
        long id = dish.getId();
        BloomFilter.bloomFilter.add((int) id);
       dishFlavor.forEach(dishFlavor1 -> {
           dishFlavor1.setDishId(id);
       });
        dishMapper.batchflavor(dishFlavor);


    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        List<DishFlavor> dishFlavor = dishDTO.getFlavors();
        dishMapper.updateDish(dish);
        long id = dish.getId();
        dishMapper.deleteDishById(id);
        dishFlavor.forEach(dishFlavor1 -> {
            dishFlavor1.setDishId(id);
        });
        if(dishFlavor.size()!=0)
            dishMapper.batchflavor(dishFlavor);


    }

    @Override
    public void deleteDish(List<Integer> ids) {
        dishMapper.deleteDishByIds(ids);
    }

    @Override
    public List<DishVO> dishVOBycategoryId(Integer categoryId) {
        List<Dish> dishList =dishMapper.searchEnableDishByCategoryId(categoryId,StatusConstant.ENABLE);
        List<DishVO> dishVOList = new ArrayList<>();
        dishList.forEach(dish -> {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish,dishVO);
            List<DishFlavor> flavors = dishMapper.searchDishFlavorsByDishId(dish.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        });
        return dishVOList;
    }
}
