package com.owen.springbootshop.service;

import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;

public interface ProductService {

    Product getProductById(Integer product_id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
