package com.order.mapper;

import com.dto.DishPageQueryDTO;
import com.entity.Dish;
import com.entity.DishFlavor;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.order.annotation.AutoFill;
import com.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    Page<Dish> queryPage(DishPageQueryDTO dishPageQueryDTO);
    @Update("update dish set status=#{status} where id = #{id}")
    void startOrStopStatus(Integer status,Long id);
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> searchDishByCategoryId(Integer categoryId);

    @Select("select * from dish where category_id = #{categoryId} and status = #{status}")
    List<Dish> searchEnableDishByCategoryId(Integer categoryId, Integer status);

    Dish searchDishById(long id);

    List<DishFlavor> searchDishFlavorsByDishId(long id);

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    void batchflavor(List<DishFlavor> dishFlavor);
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    void updateBatchFlavor(List<DishFlavor> dishFlavor);

    void deleteDishById(long id);
    @Update("update dish set status=#{status} where category_id=#{id}")
    void startOrStopStatusByCategoryId(Integer status, Integer id);

    void deleteDishByIds(List<Integer> ids);

    void deleteDishByCategoryIds(List<Integer> ids);

    int dishStatus(List<Long> dishIds, Integer disable);

    Integer countByMap(Map map);
    @Select("select id from dish")
    List<Integer> getId();
}
