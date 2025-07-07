package com.owen.springbootshop.model;

import java.util.Date;
import java.util.List;

public class Order {

    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;
    private Date createDated;
    private Date lastModifiedDated;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", createDated=" + createDated +
                ", lastModifiedDated=" + lastModifiedDated +
                ", orderItems=" + orderItems +
                '}';
    }

    private List<OrderItem> orderItems;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreateDated() {
        return createDated;
    }

    public void setCreateDated(Date createDated) {
        this.createDated = createDated;
    }

    public Date getLastModifiedDated() {
        return lastModifiedDated;
    }

    public void setLastModifiedDated(Date lastModifiedDated) {
        this.lastModifiedDated = lastModifiedDated;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
