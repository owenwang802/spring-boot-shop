package com.owen.springbootshop.model;

import java.util.Date;

public class Order {

    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;
    private Date createDated;
    private Date lastModifiedDated;

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
}
