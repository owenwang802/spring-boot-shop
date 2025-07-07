package com.owen.springbootshop.service;

import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.dto.OrderQueryParams;
import com.owen.springbootshop.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);

}
