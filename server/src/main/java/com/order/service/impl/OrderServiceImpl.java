package com.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.constant.MessageConstant;
import com.context.BaseContext;
import com.dto.*;
import com.entity.AddressBook;
import com.entity.OrderDetail;
import com.entity.Orders;
import com.entity.ShoppingCart;
import com.exception.AddressBookBusinessException;
import com.exception.OrderBusinessException;
import com.exception.ShoppingCartBusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.order.mapper.AddressMapper;
import com.order.mapper.OrderDetailMapper;
import com.order.mapper.OrderMapper;
import com.order.mapper.ShoppingCartMapper;
import com.order.service.OrderService;
import com.order.websocket.WebSocketServer;
import com.result.PageResult;
import com.vo.OrderPaymentVO;
import com.vo.OrderStatisticsVO;
import com.vo.OrderSubmitVO;
import com.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public OrderStatisticsVO statisticsOrder() {
        //统计待派送数量
        int confirmed = orderMapper.statistics(Orders.CONFIRMED);
        //统计派送中数量
        int deliveryInProgress = orderMapper.statistics(Orders.DELIVERY_IN_PROGRESS);
        //统计待接单数量
        int toBeConfirmed = orderMapper.statistics(Orders.TO_BE_CONFIRMED);
        OrderStatisticsVO orderStatisticsVO = OrderStatisticsVO.builder()
                .confirmed(confirmed).toBeConfirmed(toBeConfirmed)
                .deliveryInProgress(deliveryInProgress)
                .build();
        return orderStatisticsVO;
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> orders = orderMapper.conditionSearch(ordersPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(orders.getTotal());
        pageResult.setRecords(orders.getResult());
        return pageResult;
    }

    @Override
    public OrderVO detailsById(long id) {
        List<OrderDetail> orderDetails = orderMapper.orderDetailByOrderId((int) id);
        Orders orders = orderMapper.ordersById((int) id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        long id = ordersCancelDTO.getId();
        Orders orders = orderMapper.ordersById(id);
        if(orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (orders.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 订单处于待接单状态下取消，需要进行退款
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.cancelOrder(ordersCancelDTO);

    }

    @Override
    public void complete(Integer id) {
        orderMapper.complete(id);
    }

    @Override
    public void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO) {
        orderMapper.rejectionOrder(ordersRejectionDTO);
    }

    @Override
    public void confirmOrder(long id) {
        orderMapper.confirmOrder(id);
    }

    @Override
    public void deliveryOrder(Integer id) {
        orderMapper.deliveryOrder(id);
    }

    @Override
    public void reminderOrder(long id) {
        //查询是否存在订单
        Orders orders = orderMapper.ordersById(id);
        //如果存在，使用 websocket 进行催单
        if(orders==null ){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("type",2);
        map.put("orderId",id);
        map.put("content","订单号"+orders.getNumber());
        String json = JSON.toJSONString(map);
        //通过websocket向服务端推送消息
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public PageResult historyOrders(int page, int pageSize, Integer status) {
        PageResult pageResult = new PageResult();
        //获取用户id
        long id = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        PageHelper.startPage(page,pageSize);
        Page<Orders> ordersPage = orderMapper.queryById(id , status);
        List<OrderVO> list = new ArrayList();
        if(ordersPage!=null && ordersPage.getTotal()>0){
            for (Orders orders : ordersPage) {
                // 查询订单明细
                Long orderId = orders.getId();
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                list.add(orderVO);
            }
        }
        pageResult.setRecords(list);
        pageResult.setTotal(list.size());
        return pageResult;
    }

    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        AddressBook addressBook = addressMapper.queryById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //查询当前用户的购物车数据
        Long id = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.queryshoppingCartByUserId(id);
        if (list == null || list.size() == 0) {
            //抛出业务异常
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(id);

        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //3.向订单明细表插入n条数据
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单ID
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        //4.清空当前用户购物车数据
        shoppingCartMapper.deleteByUserId(id);
        //5.封装VO返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();



        return orderSubmitVO;
    }

    @Override
    public OrderPaymentVO payment(PaymentDTO paymentDTO) {
        String orderNumber = paymentDTO.getOrderNumber();
        Integer payMethod = paymentDTO.getPayMethod();
        Orders orders = orderMapper.searchByNumberId(orderNumber);
        if(orders==null)
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        orderMapper.updateOrder(Orders.TO_BE_CONFIRMED, payMethod, orderNumber);

        //通过websocket向客户端浏览器推送消息 type orderId content
        Map map = new HashMap();
        map.put("type",1);
        map.put("orderId",orders.getId());
        map.put("content","订单号"+orders.getNumber());
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);

        return null;
    }

    @Override
    public void repetition(long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.addShoppingCarts(shoppingCartList);
    }


}
