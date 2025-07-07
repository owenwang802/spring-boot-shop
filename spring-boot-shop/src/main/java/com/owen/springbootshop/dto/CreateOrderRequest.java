package com.owen.springbootshop.dto;

import com.owen.springbootshop.model.OrderItem;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateOrderRequest {
    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "buyItemList=" + buyItemList +
                '}';
    }

    // List or Map 集合不能為空
    @NotEmpty
    private List<BuyItem> buyItemList;

    public List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
