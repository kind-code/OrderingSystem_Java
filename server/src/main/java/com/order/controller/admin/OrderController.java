package com.order.controller.admin;

import com.dto.OrdersCancelDTO;
import com.dto.OrdersConfirmDTO;
import com.dto.OrdersPageQueryDTO;
import com.dto.OrdersRejectionDTO;
import com.order.service.OrderService;
import com.result.PageResult;
import com.result.Result;
import com.vo.OrderStatisticsVO;
import com.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/statistics")
    public Result statisticsOrder(){
        OrderStatisticsVO orderStatisticsVO = orderService.statisticsOrder();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单查询{}",ordersPageQueryDTO.toString());
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> detailsById(@PathVariable("id") Integer id){
        OrderVO orderVO = orderService.detailsById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable("id") Integer id){
        orderService.complete(id);
        return Result.success();
    }
    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejectionOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        long id = ordersConfirmDTO.getId();
        log.info("确认订单编号{}",id);
        orderService.confirmOrder(id);
        return Result.success();
    }

    @PutMapping("delivery/{id}")
    public Result deliveryOrder(@PathVariable("id") Integer id){
        orderService.deliveryOrder(id);
        return Result.success();
    }
}
