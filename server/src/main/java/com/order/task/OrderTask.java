package com.order.task;

import com.entity.Orders;
import com.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的方法
     */
    @Scheduled(cron = "0 * * * * ?")//每分触发一次
//    @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> StatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        //select * from orders where status=? and order_time < ?(当前时间-15min)

        if(StatusAndOrderTimeLT!=null && StatusAndOrderTimeLT.size()>0){
            for (Orders orders : StatusAndOrderTimeLT) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送中的订单状态
     */
    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点出发
//    @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单{}",LocalDateTime.now());

        List<Orders> StatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(-1));
        if(StatusAndOrderTimeLT!=null && StatusAndOrderTimeLT.size()>0){
            for (Orders orders : StatusAndOrderTimeLT) {
                orders.setStatus(Orders.COMPLETED);
//                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
