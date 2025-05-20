package com.owen.springbootshop.test;

import com.owen.springbootshop.constant.ProductCategory;

public class MyTest {
    // ENUM 類型的用法
    public static void main(String[] args) {
        ProductCategory productCategory = ProductCategory.E_BOOK;

        System.out.println(productCategory.name());

        String s2 = "CAR1";

        //ProductCategory category2 = ProductCategory.valueOf(s2);

        //System.out.println(category2.name());
    }
}
