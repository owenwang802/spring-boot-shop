package com.owen.springbootshop.service;

import com.owen.springbootshop.dto.CreateOrderRequest;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
