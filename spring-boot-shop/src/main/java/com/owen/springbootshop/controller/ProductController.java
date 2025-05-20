package com.owen.springbootshop.controller;

import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // {id} 要對應到 PathVariable 的 id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {

        Product product = productService.getProductById(id);
        if (product != null) {
            // 4-14 常見 http status code
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }
    // 4-13 驗證請求參數
    // PostMapping -> RequestBody
    // Validated -> 有用NotNull
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);
        // 為什麼新增完要回傳回去？
        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }



}
