package com.owen.springbootshop.dao;

import com.owen.springbootshop.constant.ProductCategory;
import com.owen.springbootshop.dto.ProductQueryParams;
import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;

import java.util.List;

public interface ProductDao {

    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    void updateStock(Integer productId, Integer stock);



}
