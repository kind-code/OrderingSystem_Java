package com.order.service.impl;

import com.constant.MessageConstant;
import com.constant.StatusConstant;
import com.dto.SetmealDTO;
import com.dto.SetmealPageQueryDTO;
import com.entity.Dish;
import com.entity.Setmeal;
import com.entity.SetmealDish;
import com.exception.SetmealEnableFailedException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.order.BloomFilter.BloomFilter;
import com.order.mapper.DishMapper;
import com.order.mapper.SetmealMapper;
import com.order.service.SetmealService;
import com.result.PageResult;
import com.vo.DishItemVO;
import com.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    public PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> setmealPage = setmealMapper.queryPage(setmealPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setRecords(setmealPage.getResult());
        pageResult.setTotal(setmealPage.getTotal());
        return pageResult;
    }

    @Override
    @Transactional
    public SetmealVO setmealById(Integer id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal= setmealMapper.querySetmealById(id);
        BeanUtils.copyProperties(setmeal,setmealVO);
        List<SetmealDish> setmealDish = setmealMapper.queryDishBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDish);
        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        List<SetmealDish> setmealDish = setmealDTO.getSetmealDishes();
        setmealMapper.updateSetmeal(setmealDTO);
        long id = setmealDTO.getId();
        List<Integer> integers = new ArrayList<>();
        integers.add((int)id);
        setmealMapper.deleteSetmealDishByids(integers);
        setmealDish.forEach(setmealDish1 -> {
            setmealDish1.setSetmealId(id);
        });
        setmealMapper.insertBatchSetmealDish(setmealDish);
    }

    @Override
    public void startOrStopSetmeal(Integer status, Integer id) {
        if(status==0){
            setmealMapper.startOrStopSetmeal(status,id);
        }else if(status == 1){
            List<Long> DishIds = new ArrayList<>();
            List<SetmealDish> setmealDish = setmealMapper.queryDishBySetmealId(id);
            setmealDish.forEach(setmealDish1 -> {
                DishIds.add(setmealDish1.getDishId());
            });
            //查询状态
           int num = dishMapper.dishStatus(DishIds, StatusConstant.DISABLE);
           if(num==0)
               //更改套餐状态
               setmealMapper.startOrStopSetmeal(status,id);
               else{
                   throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }

        }
    }

    @Override
    @Transactional
    public void deleteSetmeal(List<Integer> ids) {
        setmealMapper.deleteSetmealByids(ids);
        setmealMapper.deleteSetmealDishByids(ids);
    }

    @Override
    @Transactional
    public void insertSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insertSetmeal(setmeal);
        long setmealId = setmeal.getId();
        BloomFilter.bloomFilter.add((int)setmealId);
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealMapper.insertBatchSetmealDish(setmealDishes);
    }

    @Override
    public List<Setmeal> setmealBycategoryId(Integer categoryId) {
        List<Setmeal> setmealList = setmealMapper.searchSetmealBycategoryId(categoryId,StatusConstant.ENABLE);

        return setmealList;
    }

    @Override
    public List<DishItemVO> getDishById(Integer id) {
        List<SetmealDish> setmealDishes = setmealMapper.queryDishBySetmealId(id);
        ArrayList<DishItemVO> dishItemVOS = new ArrayList<>();
        setmealDishes.forEach(setmealDish -> {
           Dish dish = dishMapper.searchDishById(setmealDish.getDishId());
            DishItemVO dishItemVO = DishItemVO.builder()
                    .image(dish.getImage())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .copies(setmealDish.getCopies())
                    .build();
            dishItemVOS.add(dishItemVO);
        });

        return dishItemVOS;
    }
}
