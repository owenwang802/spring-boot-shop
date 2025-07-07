package com.owen.springbootshop.controller;

import com.owen.springbootshop.dto.CreateOrderRequest;
import com.owen.springbootshop.dto.OrderQueryParams;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.model.User;
import com.owen.springbootshop.service.OrderService;
import com.owen.springbootshop.service.UserService;
import com.owen.springbootshop.utils.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.awt.print.Pageable;
import java.util.List;
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

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryParams);

        Integer count = orderService.countOrder(orderQueryParams);
        // 分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);


        return ResponseEntity.status(HttpStatus.OK).body(page);



    }


}
