package com.owen.springbootshop.service.impl;


import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dto.ProductQueryParams;
import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {



        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer id) {

        return productDao.getProductById(id);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        return  productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }
}
