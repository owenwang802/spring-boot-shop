package com.owen.springbootshop.dao.impl;

import com.owen.springbootshop.constant.ProductCategory;
import com.owen.springbootshop.dao.ProductDao;
import com.owen.springbootshop.dto.ProductQueryParams;
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
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";


        Map<String,Object> params = new HashMap<>();

        if (productQueryParams.getCategory() != null) {
            // 要預留空白鍵
            sql += " AND category = :category";
            params.put("category", productQueryParams.getCategory().name());
        }
        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :search";
            params.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        // 取得Count值
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);

        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        // where 1=1 -> 方便串接
        // Spring data JPA 不會有這個問題
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM  product WHERE 1=1";

        Map<String,Object> params = new HashMap<>();

        if (productQueryParams.getCategory() != null) {
            // 要預留空白鍵
            sql += " AND category = :category";
            params.put("category", productQueryParams.getCategory().name());
        }
        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :search";
            params.put("search", "%" + productQueryParams.getSearch() + "%");
        }
        // 只能用字串拼接
        // 為什麼不用判斷？ -> defaultValue的關係 一定會有值
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        // limit offset 分頁
        sql += " LIMIT :limit OFFSET :offset";

        params.put("limit", productQueryParams.getLimit());
        params.put("offset", productQueryParams.getOffset());


        List<Product> productList = namedParameterJdbcTemplate.query(sql, params, new ProductRowMapper());

        return productList;

    }

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

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category =:category, image_url =:imageUrl, " +
                "price =:price, stock =:stock, description =:description, last_modified_date =:lastModifiedDate WHERE product_id = :productId";

        Map<String, Object> params = new HashMap<>();

        params.put("productId", productId);

        params.put("productName",productRequest.getProductName());
        params.put("category",productRequest.getCategory().toString());
        params.put("imageUrl",productRequest.getImageUrl());
        params.put("price",productRequest.getPrice());
        params.put("stock",productRequest.getStock());
        params.put("description",productRequest.getDescription());

        params.put("lastModifiedDate",new Date());

        namedParameterJdbcTemplate.update(sql, params);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";
        Map<String, Object> params = new HashMap<>();

        params.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, params);

    }
}
