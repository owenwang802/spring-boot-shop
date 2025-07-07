package com.owen.springbootshop.controller;

import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.User;
import com.owen.springbootshop.service.OrderService;
import com.owen.springbootshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private OrderService orderService;

    // /users/{userId}/orders 使用者一定要先有帳號，才能去創建訂單
    // 所以要先驗證帳號
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {


        Integer orderId = orderService.createOrder(userId, createOrderRequest);
        logger.info("orderId: {}", orderId);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


}
