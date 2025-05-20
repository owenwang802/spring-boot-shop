package com.owen.springbootshop.service;

import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Integer product_id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);


}
