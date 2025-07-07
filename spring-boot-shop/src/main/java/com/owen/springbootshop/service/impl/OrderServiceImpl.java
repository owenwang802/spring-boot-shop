package com.owen.springbootshop.service.impl;

import com.owen.springbootshop.dao.OrderDao;
import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dao.UserDao;
import com.owen.springbootshop.dto.BuyItem;
import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.dto.OrderQueryParams;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.OrderItem;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.model.User;
import com.owen.springbootshop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {


    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        logger.info("userId = {}", userId);
        logger.info("CreateOrderRequest = {}", createOrderRequest);

        // 檢查userid 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            logger.warn("user 不存在 = {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 計算總花費資訊 -> total_amount
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem:createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查產頻是否存在
            if (product == null) {
                logger.warn("商品不存在 = {}", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                logger.warn("商品 {} 庫存數量不足 無法夠買，剩餘庫存 {} ， 欲購買數量  {}",
                            buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

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

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order:orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItems(orderItemList);

        }


        return orderList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {


        return orderDao.countOrder(orderQueryParams);
    }
}
