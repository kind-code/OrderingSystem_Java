package com.order.service;

import com.dto.*;
import com.result.PageResult;
import com.vo.OrderPaymentVO;
import com.vo.OrderStatisticsVO;
import com.vo.OrderSubmitVO;
import com.vo.OrderVO;

public interface OrderService {
    OrderStatisticsVO statisticsOrder();

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO detailsById(long id);

    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void complete(Integer id);

    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    void confirmOrder(long id);

    void deliveryOrder(Integer id);

    void reminderOrder(long id);

    PageResult historyOrders(int page, int pageSize, Integer status);


    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(PaymentDTO paymentDTO);

    void repetition(long id);
}
