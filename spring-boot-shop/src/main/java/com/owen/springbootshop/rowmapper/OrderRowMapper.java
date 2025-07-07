package com.owen.springbootshop.rowmapper;

import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {

        Order order = new Order();

        order.setOrderId(resultSet.getInt("order_id"));
        order.setCreateDated(resultSet.getTimestamp("created_date"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setTotalAmount(resultSet.getInt("total_amount"));
        order.setLastModifiedDated(resultSet.getTimestamp("last_modified_date"));

        return order;
    }
}
