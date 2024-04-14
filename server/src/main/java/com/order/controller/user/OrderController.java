package com.order.controller.user;

import com.dto.OrdersCancelDTO;
import com.dto.OrdersPageQueryDTO;
import com.dto.OrdersSubmitDTO;
import com.dto.PaymentDTO;
import com.order.service.OrderService;
import com.result.PageResult;
import com.result.Result;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import com.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/reminder/{id}")
    public Result reminderOrder(@PathVariable("id") long id){
        orderService.reminderOrder(id);
       return Result.success();
    }



    /**
     * 历史订单查询
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(int page,int pageSize, Integer status){
        PageResult pageResult = orderService.historyOrders(page, pageSize, status);
        return Result.success(pageResult);
    }
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable("id") long id){
        OrdersCancelDTO ordersCancelDTO = new OrdersCancelDTO();
        ordersCancelDTO.setId(id);
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 查看订单详情
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable("id") long id){
        OrderVO orderVO = orderService.detailsById(id);
        return Result.success(orderVO);

    }

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
    return Result.success(orderSubmitVO);
    }

    /**
     * todo 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable("id") long id){
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 订单支付
     */

    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody PaymentDTO paymentDTO){
        OrderPaymentVO orderPaymentVO = orderService.payment(paymentDTO);
        return Result.success(orderPaymentVO);
    }


}
