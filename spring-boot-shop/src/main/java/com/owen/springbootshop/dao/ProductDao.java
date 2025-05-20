package com.owen.springbootshop.dao;

import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;

public interface ProductDao {

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
