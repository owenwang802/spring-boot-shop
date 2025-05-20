package com.owen.springbootshop.controller;

import com.owen.springbootshop.constant.ProductCategory;
import com.owen.springbootshop.dto.ProductQueryParams;
import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.service.ProductService;
import com.owen.springbootshop.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

// 要加上去 @Max @Min 才會真的生效 4-13
@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // Filtering 查詢條件
    // sorting 排序
    // Pagination 分頁
    // RestfulAPI Products 一定要是複數
    // required false 不是必要的
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(

           // Filtering 查詢條件
           @RequestParam(required = false) ProductCategory category,
           @RequestParam(required = false) String search,

           // Sorting 排序
           @RequestParam(defaultValue = "created_date") String orderBy,
           @RequestParam(defaultValue = "desc") String sort,

           // Pagination 分頁
           @RequestParam (defaultValue = "5") @Max(1000) @Min(0) Integer limit,
           @RequestParam (defaultValue = "0") @Min(0) Integer offset

    ) {

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得product list
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得商品總筆數
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);


        return ResponseEntity.status(HttpStatus.OK).body(page);

    }

    // 4-11 階層概念
    // {id} 要對應到 PathVariable 的 id
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {

        Product product = productService.getProductById(productId);
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

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        // 檢查 Product 是否存在
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 修改商品數據
        productService.updateProduct(productId, productRequest);

        Product upatedProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(upatedProduct);

    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }




}
