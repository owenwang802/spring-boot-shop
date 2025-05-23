package com.owen.springbootshop.rowmapper;


import com.owen.springbootshop.constant.ProductCategory;
import com.owen.springbootshop.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {


    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {

        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));
        product.setCreateDate(resultSet.getTimestamp("created_date"));
        product.setPrice(resultSet.getInt("price"));
        product.setDescription(resultSet.getString("description"));
        product.setStock(resultSet.getInt("stock"));
        product.setImageUrl(resultSet.getString("image_url"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return product;
    }
}
