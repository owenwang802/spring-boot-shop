package com.owen.springbootshop.dao.impl;

import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dto.ProductRequest;
import com.owen.springbootshop.model.Product;
import com.owen.springbootshop.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl  implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Product getProductById(Integer id) {
        //  String SQL變數
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM  product WHERE product_id =:productId";

        Map<String,Object> params = new HashMap<>();
        params.put("productId",id);

        // row mapper 轉換成 java object
        List<Product> productList = namedParameterJdbcTemplate.query(sql, params, new ProductRowMapper());

        if (!productList.isEmpty()) {
            return productList.get(0);
        }
        return null;
    }
    // 第5章節內容
    @Override
    public Integer createProduct(ProductRequest productRequest) {

        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        Map<String, Object> params = new HashMap<>();

        params.put("productName",productRequest.getProductName());
        params.put("category",productRequest.getCategory().toString());
        params.put("imageUrl",productRequest.getImageUrl());
        params.put("price",productRequest.getPrice());
        params.put("stock",productRequest.getStock());
        params.put("description",productRequest.getDescription());

        Date now = new Date();

        params.put("createdDate",now);
        params.put("lastModifiedDate",now);
        // 儲存資料庫自動生成的ProductID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);

        int productId = keyHolder.getKey().intValue();


        return productId;
    }
}
