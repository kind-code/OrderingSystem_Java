package com.order.mapper;

import com.dto.GoodsSalesDTO;
import com.dto.OrdersCancelDTO;
import com.dto.OrdersPageQueryDTO;
import com.dto.OrdersRejectionDTO;
import com.entity.OrderDetail;
import com.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("orderTime") LocalDateTime orderTime);

    Page<Orders> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> orderDetailByOrderId(Integer id);
    @Select("select * from orders where id = #{id}")
    Orders ordersById(long id);
    void update(Orders orders);
    @Select("select count(*) from orders where status = #{toBeConfirmed}")
    int statistics(Integer toBeConfirmed);

    @Update("update orders set cancel_reason = #{cancelReason} , status = 6 where id = #{id}")
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);
    @Update("update orders set status = 5 where id = #{id}")
    void complete(Integer id);

    @Update("update orders set rejection_reason = #{rejectionReason} , status = 6 where id = #{id}")
    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);
    @Update("update orders set status = 3 where id = #{id}")
    void confirmOrder(long id);

    @Update("update orders set status = 4 where id = #{id}")
    void deliveryOrder(Integer id);

    Page<Orders> queryById(long id, Integer status);

    void insert(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders searchByNumberId(String orderNumber);
    @Update("update orders set pay_method =#{payMethod},pay_status = 1, status = #{toBeConfirmed} where number = #{orderNumber}")
    void updateOrder(Integer toBeConfirmed, Integer payMethod, String orderNumber);

    Integer countByMap(HashMap<Object, Object> map);

    Double sumByMap(HashMap<Object, Object> map);

    Integer countByMap(Map map);

    /**
     * 销量前⑩
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSaleTop(LocalDateTime begin, LocalDateTime end);
}
