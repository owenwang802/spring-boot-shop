package com.owen.springbootshop.service;

import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

}
