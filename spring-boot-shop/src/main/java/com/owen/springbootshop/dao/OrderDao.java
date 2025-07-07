package com.owen.springbootshop.dao;

import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.OrderItem;

import java.util.List;


public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItems);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

}
