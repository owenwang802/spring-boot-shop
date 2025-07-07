package com.owen.springbootshop.dao.impl;

import com.owen.springbootshop.dao.OrderDao;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.OrderItem;
import com.owen.springbootshop.rowmapper.OrderItemRowMapper;
import com.owen.springbootshop.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "INSERT INTO `order`(user_id,total_amount, created_date, last_modified_date) " +
                "VALUES(:userId,:totalAmount,:createdDate,:lastModifiedDate)";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("totalAmount", totalAmount);
        Date now = new Date();
        params.put("createdDate", now);
        params.put("lastModifiedDate", now);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        int orderId = keyHolder.getKey().intValue();

        return orderId;

    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount) " +
                "VALUES(:orderId,:productId,:quantity,:amount) " ;
        MapSqlParameterSource[] params = new MapSqlParameterSource[orderItems.size()];
        // executeBatch
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            params[i] = new MapSqlParameterSource();
            params[i].addValue("orderId", orderId);
            params[i].addValue("productId", orderItem.getProductId());
            params[i].addValue("quantity", orderItem.getQuantity());
            params[i].addValue("amount", orderItem.getAmount());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, params);

    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` WHERE order_id = :orderId";

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);

        List<Order> OrderList = namedParameterJdbcTemplate.query(sql, params, new OrderRowMapper());


        if (OrderList != null && OrderList.size() > 0) {
            return OrderList.get(0);
        } else {
            return null;
        }

    }


    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {

        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
                "FROM order_item as oi " +
                "LEFT JOIN product as p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = :orderId";

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, params, new OrderItemRowMapper());

        return orderItemList;
    }
}
