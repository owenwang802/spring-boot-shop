package com.owen.springbootshop.service.impl;

import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer id) {

        return productDao.getProductById(id);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        Integer productId = productDao.createProduct(productRequest);

        return productId;
    }
}
