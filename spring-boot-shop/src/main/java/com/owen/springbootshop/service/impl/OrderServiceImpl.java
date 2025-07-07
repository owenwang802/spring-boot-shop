package com.owen.springbootshop.service.impl;

import com.owen.springbootshop.dao.OrderDao;
import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dto.BuyItem;
import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.OrderItem;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {


    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        logger.info("userId = {}", userId);
        logger.info("CreateOrderRequest = {}", createOrderRequest);

        // 計算總花費資訊 -> total_amount
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem:createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(product.getPrice());

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {

        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItems(orderItemList);


        return order;
    }
}
