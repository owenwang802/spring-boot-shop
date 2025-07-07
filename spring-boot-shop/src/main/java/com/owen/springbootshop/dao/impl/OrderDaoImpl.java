package com.owen.springbootshop.dao.impl;

import com.owen.springbootshop.dao.OrderDao;
import com.owen.springbootshop.dto.OrderQueryParams;
import com.owen.springbootshop.dto.ProductQueryParams;
import com.owen.springbootshop.model.Order;
import com.owen.springbootshop.model.OrderItem;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.rowmapper.OrderItemRowMapper;
import com.owen.springbootshop.rowmapper.OrderRowMapper;
import com.owen.springbootshop.rowmapper.ProductRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderDaoImpl implements OrderDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
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
    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        Map<String, Object> params = new HashMap<>();

        // 先查出分頁的 order_id
        String sqlOrderIds = "SELECT o.order_id " +
                "FROM `order` o " +
                "WHERE 1=1 ";

        sqlOrderIds = addFilteringSql(sqlOrderIds, params, orderQueryParams);

        sqlOrderIds += "  LIMIT :limit OFFSET :offset";
        logger.info("limit -> {}", orderQueryParams.getLimit());
        logger.info("offset -> {}", orderQueryParams.getOffset());
        params.put("limit", orderQueryParams.getLimit());
        params.put("offset", orderQueryParams.getOffset());

        logger.info("sqlOrderIds -> {}",sqlOrderIds);


        List<Integer> orderIds = namedParameterJdbcTemplate.queryForList(sqlOrderIds, params, Integer.class);

        // 如果沒有任何符合的就直接回傳
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        logger.info("orderIds -> {}", orderIds.toString());


        String sqlOrdersWithItems = "SELECT " +
                "o.order_id, o.user_id, o.total_amount, o.created_date, o.last_modified_date, " +
                "oi.order_item_id, oi.product_id, oi.quantity, oi.amount " +
                "FROM `order` o " +
                "LEFT JOIN order_item oi ON o.order_id = oi.order_id " +
                "WHERE o.order_id IN (:orderIds)  ";

        Map<String, Object> params2 = new HashMap<>();

        params2.put("orderIds", orderIds);

        logger.info("sqlOrdersWithItems -> {}", sqlOrdersWithItems);

        Map<Integer, Order> orderMap = new LinkedHashMap<>();

        namedParameterJdbcTemplate.query(sqlOrdersWithItems, params2, rs -> {

            int orderId = rs.getInt("order_id");
            logger.info("orderId -> {}", orderId);

            Order order = orderMap.get(orderId);
            logger.info("order before put -> {}", order);

            if (order == null) {
                logger.info("order == null, creating new order");
                order = new Order();
                order.setOrderId(orderId);
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getInt("total_amount"));
                order.setCreateDated(rs.getTimestamp("created_date"));
                order.setLastModifiedDated(rs.getTimestamp("last_modified_date"));
                order.setOrderItems(new ArrayList<>());
                orderMap.put(orderId, order);
                logger.info("put order into map -> {}", order);
            } else {
                logger.info("order already exists in map -> {}", order);
            }
            Integer orderItemId = rs.getObject("order_item_id", Integer.class);
            if (orderItemId != null) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(orderItemId);
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setAmount(rs.getInt("amount"));
                order.getOrderItems().add(item);
                logger.info("added order item -> {}", item);
            }
            logger.info("orderMap after while -> {}", orderMap);
        });
        return new ArrayList<>(orderMap.values());
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT count(*) FROM `order` WHERE 1=1";

        Map<String,Object> params = new HashMap<>();

        sql = addFilteringSql(sql, params, orderQueryParams);

        // 取得Count值
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);

        return total;
    }
    private String addFilteringSql(String sql, Map<String, Object> params, OrderQueryParams orderQueryParams) {

        if (orderQueryParams.getUserId() != null) {
            // 要預留空白鍵
            sql += " AND user_id = :userId";
            params.put("userId", orderQueryParams.getUserId());
        }

        return sql;
    }


}
