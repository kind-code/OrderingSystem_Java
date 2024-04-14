package com.order.mapper;

import com.dto.SetmealDTO;
import com.dto.SetmealPageQueryDTO;
import com.entity.Dish;
import com.entity.Setmeal;
import com.entity.SetmealDish;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.order.annotation.AutoFill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    @Select("select id from setmeal")
    List<Integer> getId();

    Page<Setmeal> queryPage(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id=#{id}")
    Setmeal querySetmealById(long id);
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> queryDishBySetmealId(Integer id);

    void deleteSetmealDishByids(List<Integer> ids);

    void updateSetmeal(SetmealDTO setmealDTO);

    void insertBatchSetmealDish(List<SetmealDish> setmealDish);
    @Update("update setmeal set status=#{status} where id = #{id}")
    void startOrStopSetmeal(Integer status, Integer id);

    List<Integer> querySetmealByDish(Long id);

    void deleteSetmealByids(List<Integer> ids);
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);

    @Select("select * from setmeal where status=#{enable} and category_id=#{categoryId}")
    List<Setmeal> searchSetmealBycategoryId(Integer categoryId, Integer enable);

    Integer countByMap(Map map);
}
